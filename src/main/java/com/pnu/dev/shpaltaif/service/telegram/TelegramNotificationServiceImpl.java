package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramNotificationServiceImpl implements TelegramNotificationService {

    private String appBasePath;

    private TelegramBotUserService telegramBotUserService;

    private TelegramMessageSender telegramMessageSender;

    private FreemarkerTemplateResolver freemarkerTemplateResolver;

    public TelegramNotificationServiceImpl(TelegramBotUserService telegramBotUserService,
                                           TelegramMessageSender telegramMessageSender,
                                           FreemarkerTemplateResolver freemarkerTemplateResolver,
                                           @Value("${app.base_path}") String appBasePath) {

        this.telegramBotUserService = telegramBotUserService;
        this.telegramMessageSender = telegramMessageSender;
        this.freemarkerTemplateResolver = freemarkerTemplateResolver;
        this.appBasePath = appBasePath;
    }

    @Override
    @Async
    public void sendNotificationsOfNewPost(Post post) {

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("postId", post.getId());
        templateParams.put("appBasePath", appBasePath);

        String messageContent = freemarkerTemplateResolver.resolve("/telegram/newPost.ftl", templateParams);

        Pageable pageable = PageRequest.of(0, 10);

        Page<TelegramBotUser> telegramBotUsersToNotify;
        do {

            telegramBotUsersToNotify = telegramBotUserService.findAllByCategory(post.getCategory(), pageable);

            telegramBotUsersToNotify.forEach(telegramBotUser ->
                    telegramMessageSender.sendMessageHtml(telegramBotUser.getChatId(), messageContent)
            );

            pageable = pageable.next();
        } while (!telegramBotUsersToNotify.isLast());

    }

    @Override
    @Async
    public void sendNotificationsOfNewCategory(Category category) {

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("category", category);
        templateParams.put("appBasePath", appBasePath);

        String messageContent = freemarkerTemplateResolver
                .resolve("/telegram/newCategory.ftl", templateParams);

        Pageable pageable = PageRequest.of(0, 10);

        Page<TelegramBotUser> telegramBotUsersToNotify;
        do {

            telegramBotUsersToNotify = telegramBotUserService.findAll(pageable);

            telegramBotUsersToNotify.forEach(telegramBotUser ->
                    telegramMessageSender.sendMessageHtml(telegramBotUser.getChatId(), messageContent)
            );

            pageable = pageable.next();
        } while (!telegramBotUsersToNotify.isLast());
    }
}
