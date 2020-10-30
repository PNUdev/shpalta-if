package com.pnu.dev.shpaltaif.integration.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.listener.ApplicationReadyEventListener;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import com.pnu.dev.shpaltaif.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceIntegrationTest extends BaseIntegrationTest {

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";

    private static final String NAME = "name";

    private static final String SURNAME = "surname";

    private static final CreateUserDto CREATE_USER_DTO = CreateUserDto.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .repeatedPassword(PASSWORD)
            .name(NAME)
            .surname(SURNAME)
            .build();

    @Autowired
    private UserService userService;

    @Autowired
    private PublicAccountRepository publicAccountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean // present here to disable default admin user creation
    private ApplicationReadyEventListener applicationReadyEventListener;

    @Test
    @Transactional
    public void createThenDeactivateThenActivate() {

        // Create
        User actualUser = createAndSaveUser();
        Long actualUserId = actualUser.getId();

        // Deactivate
        userService.deactivate(actualUserId);

        User deactivatedUser = userService.findById(actualUserId);
        assertFalse(deactivatedUser.isActive());

        // Activate
        userService.activate(actualUserId);
        User activatedUser = userService.findById(actualUserId);
        assertTrue(activatedUser.isActive());

    }

    @Test
    @Transactional
    public void createThenDeactivateThenDelete() {

        // Create
        User actualUser = createAndSaveUser();
        Long actualUserId = actualUser.getId();

        // Deactivate
        userService.deactivate(actualUserId);

        User deactivatedUser = userService.findById(actualUserId);
        assertFalse(deactivatedUser.isActive());

        // Delete
        userService.delete(actualUserId);

        ServiceException thrown = assertThrows(ServiceException.class,
                () -> userService.findById(actualUserId));
        assertEquals("Користувача не знайдено!", thrown.getMessage());
        assertEquals(0, userService.findAll().size());
    }

    @Test
    @Transactional
    public void createThenTryToDeleteActive() {

        // Create
        User actualUser = createAndSaveUser();

        // Try to delete
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> userService.delete(actualUser.getId()));
        assertEquals("Користувач повинен бути неактивним, щоб його можна було видалити", thrown.getMessage());
    }

    @Test
    @Transactional
    public void createThenDeactivateThenTryToDeleteUserWithPosts() {
        // Create
        User actualUser = createAndSaveUser();

        // Add posts to user
        Category category = Category.builder()
                .title("title")
                .colorTheme("colorTheme")
                .build();

        Post post = Post.builder()
                .title("title")
                .authorPublicAccount(actualUser.getPublicAccount())
                .category(category)
                .build();

        categoryRepository.save(category);
        postRepository.save(post);

        // Try to delete user with posts
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> userService.delete(actualUser.getId()));
        assertEquals("Користувач повинен бути неактивним, щоб його можна було видалити", thrown.getMessage());
    }

    private User createAndSaveUser() {
        List<User> usersBeforeCreate = userService.findAll();
        assertEquals(0, usersBeforeCreate.size());

        userService.create(CREATE_USER_DTO);

        List<User> usersAfterCreate = userService.findAll();
        assertEquals(1, usersAfterCreate.size());

        User expectedUser = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .role(UserRole.ROLE_WRITER)
                .active(true)
                .build();

        PublicAccount expectedPublicAccount = PublicAccount.builder()
                .name(NAME)
                .surname(SURNAME)
                .user(expectedUser)
                .build();

        expectedUser.setPublicAccount(expectedPublicAccount);

        User actualUser = usersAfterCreate.get(0);

        assertThat(actualUser)
                .isEqualToIgnoringGivenFields(expectedUser, "id", "publicAccount", "password");

        assertTrue(bCryptPasswordEncoder.matches(PASSWORD, actualUser.getPassword()));

        assertThat(actualUser.getPublicAccount())
                .isEqualToIgnoringGivenFields(expectedPublicAccount, "id", "createdAt", "updatedAt", "user");

        Optional<PublicAccount> actualPublicAccount = publicAccountRepository.findByUserId(actualUser.getId());
        assertEquals(actualUser.getPublicAccount(), actualPublicAccount.get());

        return actualUser;
    }

}