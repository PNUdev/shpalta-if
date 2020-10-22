package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TelegramNotificationServiceImpl implements TelegramNotificationService {

    @Value("${app.base_path}")
    private String appBasePath;

    private TelegramBotUserService telegramBotUserService;

    private TelegramMessageSender telegramMessageSender;

    private FreemarkerTemplateResolver freemarkerTemplateResolver;

    public TelegramNotificationServiceImpl(TelegramBotUserService telegramBotUserService,
                                           TelegramMessageSender telegramMessageSender,
                                           FreemarkerTemplateResolver freemarkerTemplateResolver) {

        this.telegramBotUserService = telegramBotUserService;
        this.telegramMessageSender = telegramMessageSender;
        this.freemarkerTemplateResolver = freemarkerTemplateResolver;
    }

    @Override
    @Async
    public void sendNotificationsOfNewPost(Post post) {

        List<TelegramBotUser> telegramBotUsersToNotify = telegramBotUserService // ToDo probably, have to use batch strategy
                .findAllByCategoryId(post.getCategory());

        telegramBotUsersToNotify.forEach(telegramBotUser -> {

            String messageContent = freemarkerTemplateResolver.resolve(
                    "/telegram/newPost.ftl",
                    Collections.singletonMap("postUrl", String.format("%s/posts/%s", appBasePath, post.getId()))
            );

            telegramMessageSender
                    .sendMessageHtml(telegramBotUser.getChatId(), messageContent);
        });

    }

}
