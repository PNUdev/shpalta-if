package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.dto.telegram.CategorySubscriptionsInfo;
import com.pnu.dev.shpaltaif.dto.telegram.TelegramSubscriptionsDashboardInfo;
import com.pnu.dev.shpaltaif.dto.telegram.TelegramUserCategorySubscription;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.TelegramBotUserRepository;
import com.pnu.dev.shpaltaif.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelegramBotUserServiceImpl implements TelegramBotUserService {

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
    public Page<TelegramBotUser> findAllByCategory(Category category, Pageable pageable) {
        return telegramBotUserRepository.findAllSubscribedBySubscribedCategoriesContains(category, pageable);
    }

    @Override
    public TelegramSubscriptionsDashboardInfo getSubscriptionsDashboardInfo() {

        List<Category> categories = categoryService.findAll();

        long totalUsersCount = telegramBotUserRepository.count();

        List<CategorySubscriptionsInfo> subscriptionsInfos = categories.stream()
                .map(category -> {

                    long subscribedUsersCount = telegramBotUserRepository
                            .countAllBySubscribedCategoriesContains(category);

                    return CategorySubscriptionsInfo.builder()
                            .category(category)
                            .subscribedUsersCount(subscribedUsersCount)
                            .percentOfTotalUsersCount(
                                    calculatePercentOfTotalUsersCount(subscribedUsersCount, totalUsersCount)
                            )
                            .build();
                })
                .collect(Collectors.toList());

        return TelegramSubscriptionsDashboardInfo.builder()
                .totalUsersCount(totalUsersCount)
                .subscriptionsInfos(subscriptionsInfos)
                .build();
    }

    @Override
    public void updatePreviousSettingsMessageId(Long chatId, Integer messageId) {

        TelegramBotUser telegramBotUser = findTelegramBotUserById(chatId);

        TelegramBotUser updatedTelegramBotUser = telegramBotUser.toBuilder()
                .previousSettingsMessageId(messageId)
                .build();

        telegramBotUserRepository.save(updatedTelegramBotUser);

    }

    @Override
    public List<TelegramUserCategorySubscription> findUserCategorySubscriptions(Long chatId) {

        TelegramBotUser telegramBotUser = findTelegramBotUserById(chatId);

        List<Category> subscribedCategories = telegramBotUser.getSubscribedCategories();

        List<Category> allCategories = categoryService.findAll();

        return allCategories.stream()
                .map(category -> TelegramUserCategorySubscription.builder()
                        .category(category)
                        .subscribed(subscribedCategories.contains(category))
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public void toggleUserCategorySubscription(Long chatId, Long categoryId) {

        TelegramBotUser telegramBotUser = findTelegramBotUserById(chatId);

        Category category = categoryService.findById(categoryId);

        List<Category> subscribedCategories = telegramBotUser.getSubscribedCategories();

        if (subscribedCategories.contains(category)) {
            subscribedCategories.remove(category);
        } else {
            subscribedCategories.add(category);
        }

        TelegramBotUser updatedTelegramBotUser = telegramBotUser.toBuilder()
                .subscribedCategories(subscribedCategories)
                .build();

        telegramBotUserRepository.save(updatedTelegramBotUser);

    }

    private TelegramBotUser findTelegramBotUserById(Long chatId) {
        return telegramBotUserRepository.findByChatId(chatId)
                .orElseThrow(() -> new ServiceException("Не коректний ідентифікатор чату"));
    }

    private Double calculatePercentOfTotalUsersCount(long usersSubscribed, long totalUsersCount) {
        if (totalUsersCount == 0) {
            return 0.0;
        }

        return ((double) usersSubscribed) / totalUsersCount * 100;
    }

}
