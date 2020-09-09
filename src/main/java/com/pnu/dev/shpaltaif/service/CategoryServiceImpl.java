package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException("Категорію не знайдено"));
    }

    @Override
    public void create(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void update(Long id, Category category) {
        Category categoryFromDb = findById(id);

        Category updatedCategory = categoryFromDb.toBuilder()
                .title(category.getTitle())
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
