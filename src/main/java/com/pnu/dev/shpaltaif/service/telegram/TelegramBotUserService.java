package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.dto.telegram.TelegramSubscriptionsDashboardInfo;
import com.pnu.dev.shpaltaif.dto.telegram.TelegramUserCategorySubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TelegramBotUserService {

    void create(Long chatId);

    TelegramBotUser findByChatId(Long chatId);

    Page<TelegramBotUser> findAllByCategory(Category category, Pageable pageable);

    TelegramSubscriptionsDashboardInfo getSubscriptionsDashboardInfo();

    void updatePreviousSettingsMessageId(Long chatId, Integer messageId);

    List<TelegramUserCategorySubscription> findUserCategorySubscriptions(Long chatId);

    void toggleUserCategorySubscription(Long chatId, Long categoryId);

}
