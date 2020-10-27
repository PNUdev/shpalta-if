package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

        Pageable pageable = PageRequest.of(0, 10);

        Page<TelegramBotUser> telegramBotUsersToNotify;
        do {

            telegramBotUsersToNotify = telegramBotUserService.findAllByCategory(post.getCategory(), pageable);

            telegramBotUsersToNotify.forEach(telegramBotUser -> {

                String messageContent = freemarkerTemplateResolver.resolve(
                        "/telegram/newPost.ftl",
                        Collections.singletonMap("postUrl", String.format("%s/posts/%s", appBasePath, post.getId()))
                );

                telegramMessageSender.sendMessageHtml(telegramBotUser.getChatId(), messageContent);

            });

            pageable = pageable.next();
        } while (!telegramBotUsersToNotify.isLast());

    }
}
