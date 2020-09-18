package com.pnu.dev.shpaltaif;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

        assertThrows(ServiceAdminException.class,
                () -> publicAccountService.findById(Long.MAX_VALUE),
                "Акаунт не знайдено");
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
                .login("login")
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

}
