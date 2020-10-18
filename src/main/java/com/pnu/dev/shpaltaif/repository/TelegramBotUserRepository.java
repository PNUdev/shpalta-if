package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelegramBotUserRepository extends JpaRepository<TelegramBotUser, Long> {

    boolean existsByChatId(Long chatId);

    Optional<TelegramBotUser> findBySettingsToken(String settingsToken);

    //    @Query(nativeQuery = true, value = "select * from telegram_bot_user as tu " +
//            "join telegram_user_category_subscriptions as ts on tu.chat_id = ts.chat_id " +
//            "where ts.category_id = ?")
    List<TelegramBotUser> findAllSubscribedBySubscribedCategoriesContains(Category category);

}
