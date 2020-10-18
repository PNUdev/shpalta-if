package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Service
public class TelegramNotificationServiceImpl implements TelegramNotificationService {

    private TelegramBotUserService telegramBotUserService;

    private TelegramMessageSender telegramMessageSender;

    public TelegramNotificationServiceImpl(TelegramBotUserService telegramBotUserService,
                                           TelegramMessageSender telegramMessageSender) {

        this.telegramBotUserService = telegramBotUserService;
        this.telegramMessageSender = telegramMessageSender;
    }

    @Override
    public void sendNotificationAboutNewPost(Post post) { // ToDo have to be async

        List<TelegramBotUser> telegramBotUsersToNotify = telegramBotUserService // ToDo probably, have to use batch strategy
                .findAllByCategoryId(post.getCategory());

        telegramBotUsersToNotify.forEach(telegramBotUser -> {

            String messageContent = "new post!!!";
            SendMessage sendMessage = new SendMessage(telegramBotUser.getChatId(), messageContent);
            sendMessage.enableHtml(true);

            telegramMessageSender.sendMessage(sendMessage);
        });

    }

}
