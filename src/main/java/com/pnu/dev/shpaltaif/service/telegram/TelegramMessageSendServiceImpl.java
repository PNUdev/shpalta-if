package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TelegramMessageSendServiceImpl implements TelegramMessageSendService {

    private TelegramBotUserService telegramBotUserService;

    private TelegramMessageSender telegramMessageSender;

    public TelegramMessageSendServiceImpl(TelegramBotUserService telegramBotUserService,
                                          TelegramMessageSender telegramMessageSender) {

        this.telegramBotUserService = telegramBotUserService;
        this.telegramMessageSender = telegramMessageSender;
    }

    @Async
    @Override
    public void sendMessageToCategory(String messageContent, Category category) {

        sendMessageToTelegramBotUsers(messageContent, pageable ->
                telegramBotUserService.findAllByCategory(category, pageable));

    }

    @Async
    @Override
    public void sendMessageToAllCategories(String messageContent) {
        sendMessageToTelegramBotUsers(messageContent, pageable ->
                telegramBotUserService.findAll(pageable));
    }

    private void sendMessageToTelegramBotUsers(String messageContent,
                                               Function<Pageable, Page<TelegramBotUser>> telegramBotUsersFunction) {

        Pageable pageable = PageRequest.of(0, 50);

        Page<TelegramBotUser> telegramBotUsersToNotify;
        do {

            telegramBotUsersToNotify = telegramBotUsersFunction.apply(pageable);

            telegramBotUsersToNotify.forEach(telegramBotUser ->
                    telegramMessageSender.sendMessageHtml(telegramBotUser.getChatId(), messageContent)
            );

            pageable = pageable.next();
        } while (!telegramBotUsersToNotify.isLast());
    }

}
