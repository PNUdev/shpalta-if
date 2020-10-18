package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.service.TelegramNotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramPing { // ToDo remove

    private TelegramNotificationService telegramNotificationService;

    public TelegramPing(TelegramNotificationService telegramNotificationService) {
        this.telegramNotificationService = telegramNotificationService;
    }

    @GetMapping("/test")
    public void ping() {

        Post post = Post.builder()
                .category(Category.builder()
                        .id(2L)
                        .build())
                .build();

        telegramNotificationService.sendNotificationAboutNewPost(post);

    }

}
