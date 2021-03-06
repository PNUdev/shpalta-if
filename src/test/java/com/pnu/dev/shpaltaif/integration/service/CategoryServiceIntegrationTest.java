package com.pnu.dev.shpaltaif.integration.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.CategoryDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import com.pnu.dev.shpaltaif.service.CategoryService;
import com.pnu.dev.shpaltaif.service.telegram.TelegramBotUserService;
import com.pnu.dev.shpaltaif.service.telegram.TelegramNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class CategoryServiceIntegrationTest extends BaseIntegrationTest {

    private static final String TITLE = "Title";

    private static final String COLOR = "Color";

    private static final String PUBLIC_URL = "Public url";

    private static final String UPDATED_TITLE = "Updated title";

    private static final String UPDATED_COLOR = "Updated color";

    private static final String UPDATED_PUBLIC_URL = "Updated public url";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PublicAccountRepository publicAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private TelegramBotUserService telegramBotUserService;

    @MockBean
    private TelegramNotificationService telegramNotificationService;


    @Test
    void createAndThenDeleteByIdCategoryWithoutPosts() {

        Category category = createAndSaveCategory();

        categoryService.deleteById(category.getId());

        List<Category> allCategoriesAfterDelete = categoryService.findAll();
        assertThat(allCategoriesAfterDelete).hasSize(0);

    }

    @Test
    void createAndThenDeleteByIdCategoryWithPosts() {

        User user = User.builder()
                .username("username")
                .password("password")
                .role(UserRole.ROLE_WRITER)
                .build();

        userRepository.save(user);

        PublicAccount publicAccount = PublicAccount.builder()
                .name("name")
                .surname("surname")
                .profileImageUrl("profileImageUrl")
                .description("description")
                .user(user)
                .build();

        publicAccountRepository.save(publicAccount);

        Category createdCategory = createAndSaveCategory();

        Post post = Post.builder()
                .title(TITLE)
                .category(createdCategory)
                .authorPublicAccount(publicAccount)
                .build();

        postRepository.save(post);

        ServiceException thrown = assertThrows(ServiceException.class,
                () -> categoryService.deleteById(createdCategory.getId())
        );
        assertEquals("Неможливо видалити категорію, яка має пости", thrown.getMessage());

        List<Category> allCategoriesAfterDelete = categoryService.findAll();
        assertThat(allCategoriesAfterDelete).hasSize(1);

    }

    @Test
    void createAndThenUpdateCategory() {

        Category createdCategory = createAndSaveCategory();

        CategoryDto updatedCategoryDto = CategoryDto.builder()
                .title(UPDATED_TITLE)
                .colorTheme(UPDATED_COLOR)
                .publicUrl(UPDATED_PUBLIC_URL)
                .build();

        categoryService.update(createdCategory.getId(), updatedCategoryDto);

        List<Category> allCategoriesAfterUpdate = categoryService.findAll();
        assertThat(allCategoriesAfterUpdate).hasSize(1);

        Category updatedCategoryFromDb = categoryService.findById(createdCategory.getId());

        assertValidCategoryBasedOnCategoryDto(updatedCategoryDto, updatedCategoryFromDb);

    }

    @Test
    public void findByIdNotFound() {

        assertThat(categoryService.findAll()).hasSize(0);

        ServiceException thrown = assertThrows(ServiceException.class,
                () -> categoryService.findById(Long.MAX_VALUE));
        assertEquals("Категорію не знайдено", thrown.getMessage());

    }

    @Test
    public void createDuplicatedPublicUrl() {
        createAndSaveCategory();

        CategoryDto duplicateUrlCategoryDto = CategoryDto.builder()
                .title(TITLE)
                .colorTheme(COLOR)
                .publicUrl(PUBLIC_URL)
                .build();

        ServiceException thrown = assertThrows(ServiceException.class,
                () -> categoryService.create(duplicateUrlCategoryDto));
        assertEquals("URL вже використовується", thrown.getMessage());
    }

    private Category createAndSaveCategory() {
        CategoryDto categoryDto = CategoryDto.builder()
                .title(TITLE)
                .colorTheme(COLOR)
                .publicUrl(PUBLIC_URL)
                .build();

        List<Category> allCategoriesBeforeCreate = categoryService.findAll();
        assertThat(allCategoriesBeforeCreate).hasSize(0);

        categoryService.create(categoryDto);

        List<Category> allCategoriesAfterCreate = categoryService.findAll();
        assertThat(allCategoriesAfterCreate).hasSize(1);

        Category category = allCategoriesAfterCreate.get(0);

        assertValidCategoryBasedOnCategoryDto(categoryDto, category);

        verify(telegramBotUserService, only()).addCategorySubscriptionForAllUsers(category);
        verify(telegramNotificationService, only()).sendNotificationsOfNewCategory(category);

        return category;
    }


    private void assertValidCategoryBasedOnCategoryDto(CategoryDto categoryDto, Category category) {
        assertEquals(categoryDto.getTitle(), category.getTitle());
        assertEquals(categoryDto.getColorTheme(), category.getColorTheme());
        assertEquals(categoryDto.getPublicUrl(), category.getPublicUrl());
    }

}
