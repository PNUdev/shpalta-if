package com.pnu.dev.shpaltaif.integration.service;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import com.pnu.dev.shpaltaif.service.PublicAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class NewPublicAccountServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private PublicAccountService publicAccountService;

    @Autowired
    private UserRepository userRepository;

    private User mainUser;

    @BeforeEach
    @Rollback(false)
    public void setup() {

        User mainUser = User.builder()
                .username("username")
                .password("password")
                .active(true)
                .role(UserRole.ROLE_WRITER)
                .build();
        this.mainUser = userRepository.save(mainUser);
    }

    @Test
    public void findByIdNotFound() {
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> publicAccountService.findById(Long.MAX_VALUE));
        assertEquals("Акаунт не знайдено", thrown.getMessage());
    }

    @Test
    public void createAndThenReadAndThenUpdateAndThenDeleteSuccessFlow() {
        int expectedPublicAccountsNumber = 0;
        //Verify whether there isn't any PublicAccount saved in database
        assertEquals(expectedPublicAccountsNumber, publicAccountService.findAll().size());

        //Create
        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name("name")
                .surname("surname")
                .profileImageUrl("profile_image_url")
                .description("description")
                .build();

        PublicAccount createdPublicAccount = publicAccountService.create(publicAccountDto, this.mainUser);
        expectedPublicAccountsNumber++;

        //Verify whether PublicAccount has been saved in database
        assertEquals(expectedPublicAccountsNumber, publicAccountService.findAll().size());

        PublicAccount expectedPublicAccount = PublicAccount.builder()
                .name(publicAccountDto.getName())
                .surname(publicAccountDto.getSurname())
                .profileImageUrl(publicAccountDto.getProfileImageUrl())
                .description(publicAccountDto.getDescription())
                .user(this.mainUser)
                .build();

        assertThat(createdPublicAccount)
                .isEqualToIgnoringGivenFields(expectedPublicAccount, "id", "createdAt", "updatedAt");

        // Read
        PublicAccount foundByIdPublicAccount = publicAccountService.findById(createdPublicAccount.getId());
        assertEquals(createdPublicAccount, foundByIdPublicAccount);

        // Update
        PublicAccountDto updatePublicAccountDto = PublicAccountDto.builder()
                .name("updated_name")
                .surname("updated_surname")
                .profileImageUrl("updated_profile_image_url")
                .description("updated_description")
                .build();

        publicAccountService.update(updatePublicAccountDto, createdPublicAccount.getId());
        //Verify whether the number of PublicAccounts in database hasn't changed after update
        assertEquals(expectedPublicAccountsNumber, publicAccountService.findAll().size());

        PublicAccount expectedUpdatedPublicAccount = PublicAccount.builder()
                .name(updatePublicAccountDto.getName())
                .surname(updatePublicAccountDto.getSurname())
                .profileImageUrl(updatePublicAccountDto.getProfileImageUrl())
                .description(updatePublicAccountDto.getDescription())
                .user(this.mainUser)
                .build();

        assertThat(publicAccountService.findById(createdPublicAccount.getId()))
                .isEqualToIgnoringGivenFields(expectedUpdatedPublicAccount, "id", "createdAt", "updatedAt");

        // Delete
        publicAccountService.delete(createdPublicAccount.getId());
        expectedPublicAccountsNumber--;
        //Verify whether PublicAccount has been removed from database
        assertEquals(expectedPublicAccountsNumber, publicAccountService.findAll().size());
    }

    @Test
    public void updateSignatureTest() {
        int expectedPublicAccountNumber = publicAccountService.findAll().size();
        //Create PublicAccount
        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name("name")
                .surname("surname")
                .build();
        PublicAccount createdPublicAccount = publicAccountService.create(publicAccountDto, this.mainUser);
        expectedPublicAccountNumber++;

        //Test default signature
        String expectedSignature = "name surname";
        assertEquals(expectedSignature, createdPublicAccount.getSignature());

        //Set pseudonym
        PublicAccountDto publicAccountDtoWithUnusedPseudonym = PublicAccountDto.builder()
                .name(createdPublicAccount.getName())
                .surname(createdPublicAccount.getSurname())
                .pseudonymUsed(false)
                .pseudonym("pseudonym")
                .build();
        publicAccountService.update(publicAccountDtoWithUnusedPseudonym, createdPublicAccount.getId());

        //Test signature when pseudonym has been set, but pseudonym is disabled
        PublicAccount updatedPublicAccount = publicAccountService.findById(createdPublicAccount.getId());
        assertEquals(expectedSignature, updatedPublicAccount.getSignature());

        //Activate pseudonym usage
        PublicAccountDto publicAccountDtoWithUsedPseudonym = PublicAccountDto.builder()
                .name(createdPublicAccount.getName())
                .surname(createdPublicAccount.getSurname())
                .pseudonymUsed(true)
                .pseudonym(updatedPublicAccount.getPseudonym())
                .build();
        publicAccountService.update(publicAccountDtoWithUsedPseudonym, createdPublicAccount.getId());

        //Test signature when pseudonym has been set and pseudonym is active
        PublicAccount updatedPublicAccountWithUsedPseudonym = publicAccountService.findById(createdPublicAccount.getId());
        assertEquals(publicAccountDtoWithUsedPseudonym.getPseudonym(),
                updatedPublicAccountWithUsedPseudonym.getSignature());
        assertEquals(expectedPublicAccountNumber, publicAccountService.findAll().size());
    }

    @Test
    public void updateSignatureTestExceptionFlow() {

        //Create PublicAccount
        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name("name")
                .surname("surname")
                .build();
        PublicAccount publicAccount = publicAccountService.create(publicAccountDto, this.mainUser);

        //Set blank pseudonym
        String blankPseudonym = " ";
        PublicAccountDto publicAccountDtoWithBlankSignature = PublicAccountDto.builder()
                .name(publicAccount.getName())
                .surname(publicAccount.getSurname())
                .pseudonymUsed(true)
                .pseudonym(blankPseudonym)
                .build();

        ServiceException incorrectPseudonymException = assertThrows(ServiceException.class,
                () -> publicAccountService.update(publicAccountDtoWithBlankSignature, publicAccount.getId()));
        assertEquals("Щоб використовувати псевдонім, введіть його коректно", incorrectPseudonymException.getMessage());

        //Set correct pseudonym to publicAccount
        String pseudonym = "pseudonym";
        PublicAccountDto publicAccountDtoWithPseudonym = PublicAccountDto.builder()
                .name(publicAccount.getName())
                .surname(publicAccount.getSurname())
                .pseudonymUsed(true)
                .pseudonym(pseudonym)
                .build();
        publicAccountService.update(publicAccountDtoWithPseudonym, publicAccount.getId());

        //Create another PublicAccount
        User anotherUser = User.builder()
                .username("anotherUsername")
                .password("password")
                .role(UserRole.ROLE_WRITER)
                .build();
        User anotherUserFromDb = userRepository.save(anotherUser);
        PublicAccountDto anotherPublicAccountDto = PublicAccountDto.builder()
                .name("name")
                .surname("surname")
                .build();
        PublicAccount anotherPublicAccount = publicAccountService.create(anotherPublicAccountDto, anotherUserFromDb);
        //Set booked pseudonym to AnotherPublicAccount
        PublicAccount publicAccountFromDbWithPseudonym = publicAccountService.findById(publicAccount.getId());
        String bookedPseudonym = publicAccountFromDbWithPseudonym.getPseudonym();
        PublicAccountDto publicAccountDtoWithBookedPseudonym = PublicAccountDto.builder()
                .name(anotherPublicAccount.getName())
                .surname(anotherPublicAccount.getSurname())
                .pseudonymUsed(true)
                .pseudonym(bookedPseudonym)
                .build();
        ServiceException pseudonymAlreadyBookedException = assertThrows(ServiceException.class,
                () -> publicAccountService.update(publicAccountDtoWithBookedPseudonym, anotherPublicAccount.getId()));
        assertEquals("Псевдонім зайнятий", pseudonymAlreadyBookedException.getMessage());
    }
}
