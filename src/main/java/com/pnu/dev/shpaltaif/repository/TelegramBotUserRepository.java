package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramBotUserRepository extends JpaRepository<TelegramBotUser, Long> {

    Optional<TelegramBotUser> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    Page<TelegramBotUser> findAllSubscribedBySubscribedCategoriesContains(Category category, Pageable pageable);

}
