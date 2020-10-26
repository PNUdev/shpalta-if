package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.dto.TelegramUserCategorySubscriptions;

import java.util.List;

public interface TelegramBotUserService {

    void create(Long chatId);

    TelegramBotUser findByChatId(Long chatId);

    List<TelegramBotUser> findAllByCategory(Category category);

    TelegramUserCategorySubscriptions findUserCategorySubscriptions(String settingsToken);

    void updateUserCategorySubscriptions(String settingsToken, TelegramUserCategorySubscriptions subscriptions);

}
