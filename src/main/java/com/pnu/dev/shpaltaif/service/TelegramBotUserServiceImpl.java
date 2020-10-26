package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.dto.TelegramUserCategorySubscription;
import com.pnu.dev.shpaltaif.dto.TelegramUserCategorySubscriptions;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.TelegramBotUserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelegramBotUserServiceImpl implements TelegramBotUserService { // ToDo cover by tests

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
    public TelegramBotUser findByChatId(Long chatId) {
        return telegramBotUserRepository.findByChatId(chatId)
                .orElseThrow(() -> new ServiceException("Користувача не знайдено"));
    }

    @Override
    public List<TelegramBotUser> findAllByCategory(Category category) {
        return telegramBotUserRepository.findAllSubscribedBySubscribedCategoriesContains(category);
    }

    @Override
    public TelegramUserCategorySubscriptions findUserCategorySubscriptions(String settingsToken) {

        TelegramBotUser telegramBotUser = telegramBotUserRepository.findBySettingsToken(settingsToken)
                .orElseThrow(() -> new ServiceException("Не коректний ключ до налаштувань"));

        List<Category> subscribedCategories = telegramBotUser.getSubscribedCategories();

        List<Category> allCategories = categoryService.findAll();

        List<TelegramUserCategorySubscription> subscriptions = allCategories.stream()
                .map(category -> TelegramUserCategorySubscription.builder()
                        .category(category)
                        .subscribed(subscribedCategories.contains(category))
                        .build())
                .collect(Collectors.toList());

        return TelegramUserCategorySubscriptions.builder()
                .userCategorySubscriptions(subscriptions)
                .build();
    }

    @Override
    public void updateUserCategorySubscriptions(String settingsToken, TelegramUserCategorySubscriptions subscriptions) {

        TelegramBotUser telegramBotUser = telegramBotUserRepository.findBySettingsToken(settingsToken)
                .orElseThrow(() -> new ServiceException("Не коректний ключ до налаштувань"));

        List<Category> updatedSubscribedCategories = subscriptions.getUserCategorySubscriptions().stream()
                .filter(TelegramUserCategorySubscription::isSubscribed)
                .map(TelegramUserCategorySubscription::getCategory)
                .collect(Collectors.toList());

        TelegramBotUser updatedTelegramBotUser = telegramBotUser.toBuilder()
                .subscribedCategories(updatedSubscribedCategories)
                .build();

        telegramBotUserRepository.save(updatedTelegramBotUser);

    }

}
