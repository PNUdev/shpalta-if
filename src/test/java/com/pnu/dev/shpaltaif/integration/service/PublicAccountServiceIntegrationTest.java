package com.pnu.dev.shpaltaif.integration.service;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import com.pnu.dev.shpaltaif.service.PublicAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PublicAccountServiceIntegrationTest extends BaseIntegrationTest {

    private static final String NAME = "name";

    private static final String SURNAME = "surname";

    private static final String PSEUDONYM = "pseudonym";

    private static final String PROFILE_IMAGE_URL = "profileImageUrl";

    private static final String DESCRIPTION = "description";

    private static final String UPDATED_NAME = "updatedName";

    private static final String UPDATED_SURNAME = "updatedSurname";

    private static final String UPDATED_PROFILE_IMAGE_URL = "updatedProfileImageUrl";

    private static final String UPDATED_DESCRIPTION = "updatedDescription";

    @Autowired
    private PublicAccountService publicAccountService;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void findByIdNotFound() {
        assertEquals(0, publicAccountService.findAll().size());
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> publicAccountService.findById(Long.MAX_VALUE));
        assertEquals("Акаунт не знайдено", thrown.getMessage());
    }

    @Test
    public void createAndThenReadAndThenUpdateAndThenDeleteSuccessFlow() {
        User userWriter = createAndSaveUserWriter("writer");
        assertEquals(0, publicAccountService.findAll().size());

        //Create PublicAccount
        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .description(DESCRIPTION)
                .build();

        PublicAccount createdPublicAccount = publicAccountService.create(publicAccountDto, userWriter);

        assertEquals(1, publicAccountService.findAll().size());

        PublicAccount expectedPublicAccount = PublicAccount.builder()
                .name(NAME)
                .surname(SURNAME)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .description(DESCRIPTION)
                .user(userWriter)
                .build();

        assertThat(createdPublicAccount)
                .isEqualToIgnoringGivenFields(expectedPublicAccount, "id", "createdAt", "updatedAt");

        // Read
        PublicAccount foundByIdPublicAccount = publicAccountService.findById(createdPublicAccount.getId());
        assertEquals(createdPublicAccount, foundByIdPublicAccount);

        // Update
        PublicAccountDto updatePublicAccountDto = PublicAccountDto.builder()
                .name(UPDATED_NAME)
                .surname(UPDATED_SURNAME)
                .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
                .description(UPDATED_DESCRIPTION)
                .build();

        publicAccountService.update(updatePublicAccountDto, createdPublicAccount.getId());

        assertEquals(1, publicAccountService.findAll().size());

        PublicAccount expectedUpdatedPublicAccount = PublicAccount.builder()
                .name(UPDATED_NAME)
                .surname(UPDATED_SURNAME)
                .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
                .description(UPDATED_DESCRIPTION)
                .user(userWriter)
                .build();

        assertThat(publicAccountService.findById(createdPublicAccount.getId()))
                .isEqualToIgnoringGivenFields(expectedUpdatedPublicAccount, "id", "createdAt", "updatedAt");

        // Delete
        publicAccountService.delete(createdPublicAccount.getId());
        //Verify whether PublicAccount has been removed from database
        assertEquals(0, publicAccountService.findAll().size());
    }

    @Test
    public void updateSignatureTest() {
        User userWriter = createAndSaveUserWriter("writer");
        //Create PublicAccount
        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .build();
        PublicAccount createdPublicAccount = publicAccountService.create(publicAccountDto, userWriter);

        //Test default signature
        String expectedSignature = String.format("%s %s", NAME, SURNAME);
        assertEquals(expectedSignature, createdPublicAccount.getSignature());

        //Set pseudonym
        PublicAccountDto publicAccountDtoWithUnusedPseudonym = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .pseudonymUsed(false)
                .pseudonym(PSEUDONYM)
                .build();
        publicAccountService.update(publicAccountDtoWithUnusedPseudonym, createdPublicAccount.getId());

        //Test signature when pseudonym has been set, but pseudonym is disabled
        PublicAccount updatedPublicAccount = publicAccountService.findById(createdPublicAccount.getId());
        assertEquals(expectedSignature, updatedPublicAccount.getSignature());

        //Activate pseudonym usage
        PublicAccountDto publicAccountDtoWithUsedPseudonym = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .pseudonymUsed(true)
                .pseudonym(PSEUDONYM)
                .build();
        publicAccountService.update(publicAccountDtoWithUsedPseudonym, createdPublicAccount.getId());

        //Test signature when pseudonym has been set and pseudonym is active
        PublicAccount updatedPublicAccountWithUsedPseudonym = publicAccountService.findById(createdPublicAccount.getId());
        assertEquals(publicAccountDtoWithUsedPseudonym.getPseudonym(),
                updatedPublicAccountWithUsedPseudonym.getSignature());
        assertEquals(1, publicAccountService.findAll().size());
    }

    @Test
    public void updateSignatureTestExceptionFlow() {
        User userWriter = createAndSaveUserWriter("writer");
        //Create PublicAccount
        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .build();
        PublicAccount publicAccount = publicAccountService.create(publicAccountDto, userWriter);

        //Set blank pseudonym
        String blankPseudonym = " ";
        PublicAccountDto publicAccountDtoWithBlankSignature = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .pseudonymUsed(true)
                .pseudonym(blankPseudonym)
                .build();

        ServiceException incorrectPseudonymException = assertThrows(ServiceException.class,
                () -> publicAccountService.update(publicAccountDtoWithBlankSignature, publicAccount.getId()));
        assertEquals("Щоб використовувати псевдонім, введіть його коректно", incorrectPseudonymException.getMessage());

        //Set correct pseudonym to publicAccount
        PublicAccountDto publicAccountDtoWithPseudonym = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .pseudonymUsed(true)
                .pseudonym(PSEUDONYM)
                .build();
        publicAccountService.update(publicAccountDtoWithPseudonym, publicAccount.getId());

        //Create another PublicAccount
        User anotherWriter = createAndSaveUserWriter("anotherWriter");
        PublicAccountDto anotherPublicAccountDto = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .build();
        PublicAccount anotherPublicAccount = publicAccountService.create(anotherPublicAccountDto, anotherWriter);

        //Set booked pseudonym to AnotherPublicAccount
        PublicAccount publicAccountFromDbWithPseudonym = publicAccountService.findById(publicAccount.getId());
        String bookedPseudonym = publicAccountFromDbWithPseudonym.getPseudonym();
        PublicAccountDto publicAccountDtoWithBookedPseudonym = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .pseudonymUsed(true)
                .pseudonym(bookedPseudonym)
                .build();
        ServiceException pseudonymAlreadyBookedException = assertThrows(ServiceException.class,
                () -> publicAccountService.update(publicAccountDtoWithBookedPseudonym, anotherPublicAccount.getId()));
        assertEquals("Псевдонім зайнятий", pseudonymAlreadyBookedException.getMessage());
    }

    private User createAndSaveUserWriter(String username) {
        User mainUser = User.builder()
                .username(username)
                .password("password")
                .active(true)
                .role(UserRole.ROLE_WRITER)
                .build();
        return userRepository.save(mainUser);
    }
}
