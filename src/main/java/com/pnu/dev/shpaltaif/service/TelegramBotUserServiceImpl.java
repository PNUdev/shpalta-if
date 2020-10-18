package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.TelegramBotUserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelegramBotUserServiceImpl implements TelegramBotUserService { // ToDo fix and cover by tests

    private TelegramBotUserRepository telegramBotUserRepository;

    private CategoryService categoryService;

    @Autowired
    public TelegramBotUserServiceImpl(TelegramBotUserRepository telegramBotUserRepository, CategoryService categoryService) {
        this.telegramBotUserRepository = telegramBotUserRepository;
        this.categoryService = categoryService;
    }

    @Override
    public void create(Long chatId) {

        if (telegramBotUserRepository.existsByChatId(chatId)) {
            return;
        }

        TelegramBotUser telegramBotUser = TelegramBotUser.builder()
                .chatId(chatId)
                .settingsToken(RandomStringUtils.randomAlphanumeric(15))
                .subscribedCategories(categoryService.findAll())
                .build();

        telegramBotUserRepository.save(telegramBotUser);

    }

    @Override
    public List<TelegramBotUser> findAllByCategoryId(Category category) {
        return telegramBotUserRepository.findAllSubscribedBySubscribedCategoriesContains(category);
    }

    @Override
    public void updateCategoriesSubscriptions(String settingsToken, List<Long> categoryIds) {

        TelegramBotUser telegramBotUser = telegramBotUserRepository.findBySettingsToken(settingsToken)
                .orElseThrow(() -> new ServiceException("Не коректний ключ до налаштувань"));

        List<Category> categories = categoryService.findAll().stream() // ToDo improve
                .filter(category -> categoryIds.contains(category.getId()))
                .collect(Collectors.toList());

        TelegramBotUser updatedTelegramBotUser = telegramBotUser.toBuilder()
                .subscribedCategories(categories)
                .build();

        telegramBotUserRepository.save(updatedTelegramBotUser);

    }

}
