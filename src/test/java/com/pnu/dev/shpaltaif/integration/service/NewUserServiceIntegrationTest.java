package com.pnu.dev.shpaltaif.integration.service;

import com.google.common.collect.Iterables;
import com.pnu.dev.shpaltaif.domain.*;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.listener.ApplicationReadyEventListener;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import com.pnu.dev.shpaltaif.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewUserServiceIntegrationTest extends BaseIntegrationTest {

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
    public void createWriterAndEditorUsersTest() {

        int expectedUsersNumber = userService.findAll().size();
        int expectedPublicAccountsNumber = publicAccountRepository.findAll().size();

        assertEquals(0, expectedUsersNumber);
        assertEquals(0, expectedPublicAccountsNumber);

        CreateUserDto createUserWriterInvalidDto = CreateUserDto.builder()
                .username("writer")
                .password("writer")
                .repeatedPassword("writer")
                .role(UserRole.ROLE_WRITER)
                .build();

        ServiceException thrown = assertThrows(ServiceException.class,
                () -> userService.create(createUserWriterInvalidDto));
        assertEquals("Ім'я повинно бути вказаним", thrown.getMessage());
        assertEquals(0, userService.findAll().size());
        assertEquals(0, publicAccountRepository.findAll().size());

        CreateUserDto createUserWriterValidDto = CreateUserDto.builder()
                .username("writer")
                .password("writer")
                .repeatedPassword("writer")
                .role(UserRole.ROLE_WRITER)
                .name("name")
                .surname("surname")
                .build();

        User expectedWriter = User.builder()
                .username(createUserWriterValidDto.getUsername())
                .role(UserRole.ROLE_WRITER)
                .active(true)
                .build();

        PublicAccount expectedPublicAccount = PublicAccount.builder()
                .name(createUserWriterValidDto.getName())
                .surname(createUserWriterValidDto.getSurname())
                .build();

        userService.create(createUserWriterValidDto);
        expectedUsersNumber++;
        expectedPublicAccountsNumber++;
        assertEquals(expectedUsersNumber, userService.findAll().size());
        assertEquals(1, publicAccountRepository.findAll().size());
        User createdWriter = getLastCreatedUser();
        assertThat(createdWriter)
                .isEqualToIgnoringGivenFields(expectedWriter, "id", "publicAccount", "password");
        assertTrue(bCryptPasswordEncoder.matches(createUserWriterValidDto.getPassword(), createdWriter.getPassword()));
        assertThat(createdWriter.getPublicAccount())
                .isEqualToIgnoringGivenFields(expectedPublicAccount, "id", "createdAt", "updatedAt", "user");
        CreateUserDto createUserEditorValidDto = CreateUserDto.builder()
                .username("editor")
                .password("editor")
                .repeatedPassword("editor")
                .role(UserRole.ROLE_EDITOR)
                .build();

        User expectedEditor = User.builder()
                .username(createUserEditorValidDto.getUsername())
                .role(UserRole.ROLE_EDITOR)
                .active(true)
                .build();

        userService.create(createUserEditorValidDto);
        expectedUsersNumber++;
        assertEquals(expectedUsersNumber, userService.findAll().size());
        assertEquals(expectedPublicAccountsNumber, publicAccountRepository.findAll().size());
        User createdEditor = getLastCreatedUser();
        assertThat(createdEditor).isEqualToIgnoringGivenFields(expectedEditor, "id", "password");
        assertTrue(bCryptPasswordEncoder.matches(createUserEditorValidDto.getPassword(), createdEditor.getPassword()));

    }

    @Test
    public void createThenDeactivateThenActivate() {

        // Create
        User actualUser = createAndSaveUserWriter();
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
    public void createThenDeactivateThenDelete() {

        // Create
        User actualUser = createAndSaveUserWriter();
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
    public void createThenTryToDeleteActive() {

        // Create
        User actualUser = createAndSaveUserWriter();

        // Try to delete
        ServiceException thrown = assertThrows(ServiceException.class,
                () -> userService.delete(actualUser.getId()));
        assertEquals("Користувач повинен бути неактивним, щоб його можна було видалити", thrown.getMessage());
    }

    @Test
    public void createThenDeactivateThenTryToDeleteUserWithPosts() {
        // Create
        User actualUser = createAndSaveUserWriter();

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

    private User createAndSaveUserWriter() {
        CreateUserDto createUserWriterDto = CreateUserDto.builder()
                .username("writer")
                .password("writer")
                .repeatedPassword("writer")
                .role(UserRole.ROLE_WRITER)
                .name("name")
                .surname("surname")
                .build();

        userService.create(createUserWriterDto);
        return getLastCreatedUser();
    }

    private User getLastCreatedUser() {
        return Iterables.getLast(userService.findAll());
    }

}
