package com.pnu.dev.shpaltaif;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import com.pnu.dev.shpaltaif.service.PublicAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PublicAccountServiceIntegrationTest {

    private static final String NAME = "name";

    private static final String SURNAME = "surname";

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
    @Transactional
    public void createAndThenReadAndThenUpdateAndThenDeleteSuccessFlow() {
        List<PublicAccount> publicAccountsBeforeCreate = publicAccountService.findAll();
        assertEquals(0, publicAccountsBeforeCreate.size());

        // Create
        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name(NAME)
                .surname(SURNAME)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .description(DESCRIPTION)
                .build();

        User user = User.builder()
                .username("username")
                .password("password")
                .role(UserRole.ROLE_WRITER)
                .build();

        userRepository.save(user);

        publicAccountService.create(publicAccountDto, user);

        List<PublicAccount> publicAccountsAfterCreate = publicAccountService.findAll();
        assertEquals(1, publicAccountsAfterCreate.size());

        PublicAccount expectedPublicAccount = PublicAccount.builder()
                .name(NAME)
                .surname(SURNAME)
                .profileImageUrl(PROFILE_IMAGE_URL)
                .description(DESCRIPTION)
                .user(user)
                .build();

        PublicAccount actualPublicAccount = publicAccountsAfterCreate.get(0);
        assertThat(actualPublicAccount)
                .isEqualToIgnoringGivenFields(expectedPublicAccount, "id", "createdAt", "updatedAt");

        // Read
        PublicAccount foundByIdPublicAccount = publicAccountService.findById(publicAccountsAfterCreate.get(0).getId());
        assertEquals(actualPublicAccount, foundByIdPublicAccount);

        // Update
        PublicAccountDto updatePublicAccountDto = PublicAccountDto.builder()
                .name(UPDATED_NAME)
                .surname(UPDATED_SURNAME)
                .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
                .description(UPDATED_DESCRIPTION)
                .build();

        publicAccountService.update(updatePublicAccountDto, actualPublicAccount.getId());

        List<PublicAccount> publicAccountsAfterUpdate = publicAccountService.findAll();
        assertEquals(1, publicAccountsAfterUpdate.size());

        PublicAccount expectedUpdatedPublicAccount = PublicAccount.builder()
                .name(UPDATED_NAME)
                .surname(UPDATED_SURNAME)
                .profileImageUrl(UPDATED_PROFILE_IMAGE_URL)
                .description(UPDATED_DESCRIPTION)
                .user(user)
                .build();

        assertThat(publicAccountsAfterUpdate.get(0))
                .isEqualToIgnoringGivenFields(expectedUpdatedPublicAccount, "id", "createdAt", "updatedAt");

        // Delete
        publicAccountService.delete(actualPublicAccount.getId());
        List<PublicAccount> publicAccountsAfterDelete = publicAccountService.findAll();
        assertEquals(0, publicAccountsAfterDelete.size());
    }

    @Test
    public void updateSignatureTest() {

        //Create PublicAccount
        PublicAccount publicAccount = createPublicAccount("name", "surname");

        //Test default signature
        String expectedSignature = "name surname";
        assertEquals(expectedSignature, publicAccountService.findById(publicAccount.getId()).getSignature());

        //Set pseudonym
        String pseudonym = "pseudonym";
        PublicAccountDto publicAccountDtoWithUnusedPseudonym = PublicAccountDto.builder()
                .name(publicAccount.getName())
                .surname(publicAccount.getSurname())
                .pseudonymUsed(false)
                .pseudonym(pseudonym)
                .build();
        publicAccountService.update(publicAccountDtoWithUnusedPseudonym, publicAccount.getId());

        //Test signature when pseudonym has been set, but pseudonym is disabled
        assertEquals(expectedSignature, publicAccountService.findById(publicAccount.getId()).getSignature());

        //Activate pseudonym usage
        PublicAccountDto publicAccountDtoWithUsedPseudonym = PublicAccountDto.builder()
                .name(publicAccount.getName())
                .surname(publicAccount.getSurname())
                .pseudonymUsed(true)
                .pseudonym(pseudonym)
                .build();
        publicAccountService.update(publicAccountDtoWithUsedPseudonym, publicAccount.getId());

        //Test signature when pseudonym has been set and pseudonym is active
        assertEquals(publicAccountDtoWithUsedPseudonym.getPseudonym(),
                publicAccountService.findById(publicAccount.getId()).getSignature());
    }

    @Test
    public void updateSignatureTestExceptionFlow() {

        //Create PublicAccount
        PublicAccount publicAccount = createPublicAccount("name", "surname");

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
        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name(publicAccount.getName())
                .surname(publicAccount.getSurname())
                .pseudonymUsed(true)
                .pseudonym(pseudonym)
                .build();
        publicAccountService.update(publicAccountDto, publicAccount.getId());

        //Set booked pseudonym to AnotherPublicAccount
        PublicAccount anotherPublicAccount = createPublicAccount("name", "surname");
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

    private PublicAccount createPublicAccount(String name, String surname) {

        User user = createUserWriter();

        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name(name)
                .surname(surname)
                .build();

        return publicAccountService.create(publicAccountDto, user);
    }

    private User createUserWriter() {
        int usersNumberBeforeCreate = userRepository.findAll().size();
        long userId = usersNumberBeforeCreate + 1;
        User user = User.builder()
                .id(userId)
                .username(String.format("username%s", userId))
                .password("password")
                .role(UserRole.ROLE_WRITER)
                .build();

        return userRepository.save(user);
    }


}
