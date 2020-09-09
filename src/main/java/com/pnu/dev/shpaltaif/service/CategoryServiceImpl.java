package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.dto.CategoryDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private PostRepository postRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceAdminException("Категорію не знайдено"));
    }

    @Override
    public void create(CategoryDto categoryDto) {

        Category category = Category.builder()
                .title(categoryDto.getTitle())
                .build();

        categoryRepository.save(category);
    }

    @Override
    public void update(Long id, CategoryDto categoryDto) {
        Category categoryFromDb = findById(id);

        Category updatedCategory = categoryFromDb.toBuilder()
                .title(categoryDto.getTitle())
                .build();

        categoryRepository.save(updatedCategory);
    }

    @Override
    public void deleteById(Long id) {

        if (postRepository.existsPostsByCategoryId(id)) {
            throw new SecurityException("Неможливо видалити категорію, яка має пости");
        }

        categoryRepository.deleteById(id);

    }
}
