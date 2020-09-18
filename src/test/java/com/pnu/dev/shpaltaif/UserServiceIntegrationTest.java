package com.pnu.dev.shpaltaif;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import com.pnu.dev.shpaltaif.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {

    private static final String LOGIN = "login";

    private static final String PASSWORD = "password";

    private static final String NAME = "name";

    private static final String SURNAME = "surname";

    private static final CreateUserDto CREATE_USER_DTO = CreateUserDto.builder()
            .login(LOGIN)
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

    @Test
    @Transactional
    public void createThenDeactivateThenActivate() {

        // Create
        User actualUser = createUser();
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
        User actualUser = createUser();
        Long actualUserId = actualUser.getId();

        // Deactivate
        userService.deactivate(actualUserId);

        User deactivatedUser = userService.findById(actualUserId);
        assertFalse(deactivatedUser.isActive());

        // Delete
        userService.delete(actualUserId);

        assertThrows(ServiceAdminException.class,
                () -> userService.findById(actualUserId),
                "Користувача не знайдено");

        assertEquals(0, userService.findAll().size());
    }

    @Test
    @Transactional
    public void createThenTryToDeleteActive() {

        // Create
        User actualUser = createUser();

        // Try to delete
        assertThrows(ServiceAdminException.class,
                () -> userService.delete(actualUser.getId()),
                "Користувач повинен буте неактивним, щоб його можна було видалити");
    }

    @Test
    @Transactional
    public void createThenDeactivateThenTryToDeleteUserWithPosts() {
        // Create
        User actualUser = createUser();

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
        assertThrows(ServiceAdminException.class,
                () -> userService.delete(actualUser.getId()),
                "Неможливо видалити акаунт користувача, який має існуючі пости");
    }

    private User createUser() {
        List<User> usersBeforeCreate = userService.findAll();
        assertEquals(0, usersBeforeCreate.size());

        userService.create(CREATE_USER_DTO);

        List<User> usersAfterCreate = userService.findAll();
        assertEquals(1, usersAfterCreate.size());

        User expectedUser = User.builder()
                .login(LOGIN)
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
                .isEqualToIgnoringGivenFields(expectedUser, "id", "publicAccount");

        assertThat(actualUser.getPublicAccount())
                .isEqualToIgnoringGivenFields(expectedPublicAccount, "id", "createdAt", "updatedAt", "user");


        Optional<PublicAccount> actualPublicAccount = publicAccountRepository.findByUserId(actualUser.getId());
        assertEquals(actualUser.getPublicAccount(), actualPublicAccount.get());

        return actualUser;
    }

}
