package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            Map<String, Object> params = new HashMap<>();
            params.put("post", post);
            params.put("appBasePath", appBasePath);

            String messageContent = freemarkerTemplateResolver.resolve("/telegram/newPost.ftl", params);

            telegramMessageSender.sendMessageHtml(telegramBotUser.getChatId(), messageContent);
        });

    }

}
