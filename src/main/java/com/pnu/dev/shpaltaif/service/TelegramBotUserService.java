package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.dto.TelegramUserCategorySubscriptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TelegramBotUserService {

    void create(Long chatId);

    TelegramBotUser findByChatId(Long chatId);

    Page<TelegramBotUser> findAllByCategory(Category category, Pageable pageable);

    TelegramUserCategorySubscriptions findUserCategorySubscriptions(String settingsToken);

    void updateUserCategorySubscriptions(String settingsToken, TelegramUserCategorySubscriptions subscriptions);

}
