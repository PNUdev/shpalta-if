package com.pnu.dev.shpaltaif.integration;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.CategoryDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import com.pnu.dev.shpaltaif.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CategoryServiceIntegrationTest {

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

    @Test
    void createAndThenDeleteByIdCategoryWithoutPosts() {

        Category category = createAndSaveCategory();

        categoryService.deleteById(category.getId());

        List<Category> allCategoriesAfterDelete = categoryService.findAll();
        assertEquals(0, allCategoriesAfterDelete.size());

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
        assertEquals(1, allCategoriesAfterDelete.size());

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
        assertEquals(1, allCategoriesAfterUpdate.size());

        Category updatedCategoryFromDb = categoryService.findById(createdCategory.getId());

        assertValidCategoryBasedOnCategoryDto(updatedCategoryDto, updatedCategoryFromDb);

    }

    @Test
    public void findByIdNotFound() {

        assertEquals(0, categoryService.findAll().size());

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
        assertEquals(0, allCategoriesBeforeCreate.size());

        categoryService.create(categoryDto);

        List<Category> allCategoriesAfterCreate = categoryService.findAll();
        assertEquals(1, allCategoriesAfterCreate.size());

        Category category = allCategoriesAfterCreate.get(0);

        assertValidCategoryBasedOnCategoryDto(categoryDto, category);

        return category;
    }


    private void assertValidCategoryBasedOnCategoryDto(CategoryDto categoryDto, Category category) {
        assertEquals(categoryDto.getTitle(), category.getTitle());
        assertEquals(categoryDto.getColorTheme(), category.getColorTheme());
        assertEquals(categoryDto.getPublicUrl(), category.getPublicUrl());
    }

}
