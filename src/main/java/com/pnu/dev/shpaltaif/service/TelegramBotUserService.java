package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;

import java.util.List;

public interface TelegramBotUserService {

    void create(Long chatId);

    List<TelegramBotUser> findAllByCategoryId(Category category);

    void updateCategoriesSubscriptions(String settingsToken, List<Long> categoryIds);

}
