package com.pnu.dev.shpaltaif;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.dto.CategoryDto;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryServiceIntegrationTest {

    private static final String TITLE = "Title";

    private static final String UPDATED_TITLE = "Updated title";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    CategoryService categoryService;

    @Test
    void createAndThenDeleteByIdCategoryWithoutPosts() {

        CategoryDto categoryDto = CategoryDto.builder()
                .title(TITLE)
                .build();

        List<Category> allCategoriesBeforeCreate = categoryService.findAll();
        assertEquals(0, allCategoriesBeforeCreate.size());

        categoryService.create(categoryDto);

        List<Category> allCategoriesAfterCreate = categoryService.findAll();
        assertEquals(1, allCategoriesAfterCreate.size());

        categoryService.deleteById(allCategoriesAfterCreate.get(0).getId());

        List<Category> allCategoriesAfterDelete = categoryService.findAll();
        assertEquals(0, allCategoriesAfterDelete.size());

    }

    @Test
    void createAndThenDeleteByIdCategoryWithPosts() {

        CategoryDto categoryDto = CategoryDto.builder()
                .title(TITLE)
                .build();

        List<Category> allCategoriesBeforeCreate = categoryService.findAll();
        assertEquals(0, allCategoriesBeforeCreate.size());

        categoryService.create(categoryDto);

        List<Category> allCategoriesAfterCreate = categoryService.findAll();
        assertEquals(1, allCategoriesAfterCreate.size());

        Category createdCategory = allCategoriesAfterCreate.get(0);

        Post post = Post.builder()
                .title(TITLE)
                .category(createdCategory)
                .build();

        postRepository.save(post);

        assertThrows(SecurityException.class,
                () -> categoryService.deleteById(createdCategory.getId()),
                "Неможливо видалити категорію, яка має пости"
        );

        List<Category> allCategoriesAfterDelete = categoryService.findAll();
        assertEquals(1, allCategoriesAfterDelete.size());

    }

    @Test
    void createAndThenUpdateCategoryWithPosts() {

        CategoryDto categoryDto = CategoryDto.builder()
                .title(TITLE)
                .build();

        List<Category> allCategoriesBeforeCreate = categoryService.findAll();
        assertEquals(0, allCategoriesBeforeCreate.size());

        categoryService.create(categoryDto);

        List<Category> allCategoriesAfterCreate = categoryService.findAll();
        assertEquals(1, allCategoriesAfterCreate.size());

        Category createdCategory = allCategoriesAfterCreate.get(0);

        Category expectedUpdatedCategory = createdCategory.toBuilder()
                .title(UPDATED_TITLE)
                .build();

        CategoryDto updateCategoryDto = CategoryDto.builder()
                .title(UPDATED_TITLE)
                .build();

        categoryService.update(expectedUpdatedCategory.getId(), updateCategoryDto);

        List<Category> allCategoriesAfterUpdate = categoryService.findAll();
        assertEquals(1, allCategoriesAfterUpdate.size());

        Category updatedCategoryFromDb = categoryService.findById(expectedUpdatedCategory.getId());

        // posts is lazy-fetched and we are out of hibernate session here, so we will simply ignore this field
        assertThat(updatedCategoryFromDb)
                .isEqualToIgnoringGivenFields(expectedUpdatedCategory, "posts");

    }

}
