package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.dto.CategoryDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.service.telegram.TelegramBotUserService;
import com.pnu.dev.shpaltaif.service.telegram.TelegramNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private PostRepository postRepository;

    private TelegramBotUserService telegramBotUserService;

    private TelegramNotificationService telegramNotificationService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               PostRepository postRepository,
                               TelegramNotificationService telegramNotificationService,
                               TelegramBotUserService telegramBotUserService) {

        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.telegramNotificationService = telegramNotificationService;
        this.telegramBotUserService = telegramBotUserService;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Категорію не знайдено"));
    }

    @Override
    public Category findByPublicUrl(String publicUrl) {
        return categoryRepository.findByPublicUrl(publicUrl)
                .orElseThrow(() -> new ServiceException("Категорію не знайдено"));
    }

    @Override
    public void create(CategoryDto categoryDto) {

        if (categoryRepository.existsByPublicUrl(categoryDto.getPublicUrl())) {
            throw new ServiceException("URL вже використовується");
        }

        Category category = Category.builder()
                .title(categoryDto.getTitle())
                .colorTheme(categoryDto.getColorTheme())
                .publicUrl(categoryDto.getPublicUrl())
                .build();

        categoryRepository.save(category);

        telegramBotUserService.addCategorySubscriptionForAllUsers(category);
        telegramNotificationService.sendNotificationsOfNewCategory(category);
    }

    @Override
    public void update(Long id, CategoryDto categoryDto) {
        Category categoryFromDb = findById(id);

        Category updatedCategory = categoryFromDb.toBuilder()
                .title(categoryDto.getTitle())
                .colorTheme(categoryDto.getColorTheme())
                .publicUrl(categoryDto.getPublicUrl())
                .build();

        categoryRepository.save(updatedCategory);
    }

    @Override
    public void deleteById(Long id) {

        if (postRepository.existsPostsByCategoryId(id)) {
            throw new ServiceException("Неможливо видалити категорію, яка має пости");
        }

        categoryRepository.deleteById(id);

    }
}
