package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramNotificationServiceImpl implements TelegramNotificationService {

    private String appBasePath;

    private FreemarkerTemplateResolver freemarkerTemplateResolver;

    private TelegramMessageSendService telegramMessageSendService;

    public TelegramNotificationServiceImpl(FreemarkerTemplateResolver freemarkerTemplateResolver,
                                           TelegramMessageSendService telegramMessageSendService,
                                           @Value("${app.base_path}") String appBasePath) {

        this.freemarkerTemplateResolver = freemarkerTemplateResolver;
        this.appBasePath = appBasePath;
        this.telegramMessageSendService = telegramMessageSendService;
    }

    @Async
    @Override
    public void sendNotificationsOfNewPost(Post post) {

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("postId", post.getId());
        templateParams.put("appBasePath", appBasePath);

        String messageContent = freemarkerTemplateResolver.resolve("/telegram/newPost.ftl", templateParams);

        telegramMessageSendService.sendMessageToCategory(messageContent, post.getCategory());
    }

    @Async
    @Override
    public void sendNotificationsOfNewCategory(Category category) {

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("category", category);
        templateParams.put("appBasePath", appBasePath);

        String messageContent = freemarkerTemplateResolver
                .resolve("/telegram/newCategory.ftl", templateParams);

        telegramMessageSendService.sendMessageToCategory(messageContent, category);
    }
}
