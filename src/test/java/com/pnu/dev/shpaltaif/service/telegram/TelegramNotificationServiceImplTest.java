package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TelegramNotificationServiceImplTest {

    private static final String APP_BASE_URL = "appBaseUrl";

    private static final int PAGE_SIZE = 10;

    private static final Long POST_ID = 33L;

    private static final int TOTAL_TELEGRAM_USERS_NUMBER = 15;

    private static final String MESSAGE_CONTENT = "message content";

    private static final String TEMPLATE_NAME = "/telegram/newPost.ftl";

    private static final Map<String, Object> TEMPLATE_PARAMS = Collections
            .singletonMap("postUrl", String.format("%s/posts/%s", APP_BASE_URL, POST_ID));

    private TelegramBotUserService telegramBotUserService = mock(TelegramBotUserService.class);

    private TelegramMessageSender telegramMessageSender = mock(TelegramMessageSender.class);

    private FreemarkerTemplateResolver freemarkerTemplateResolver = mock(FreemarkerTemplateResolver.class);

    private TelegramNotificationService telegramNotificationService = new TelegramNotificationServiceImpl(
            telegramBotUserService, telegramMessageSender, freemarkerTemplateResolver, APP_BASE_URL);

    @Test
    void sendNotificationsOfNewPostSuccessFlow() {

        Category category = Category.builder()
                .title("category title")
                .build();

        Post post = Post.builder()
                .id(POST_ID)
                .category(category)
                .build();

        List<TelegramBotUser> telegramBotUsersForPageOne = LongStream.range(0, PAGE_SIZE)
                .mapToObj(idx -> TelegramBotUser.builder()
                        .chatId(idx)
                        .build()
                )
                .collect(Collectors.toList());


        Pageable pageOnePageable = PageRequest.of(0, PAGE_SIZE);

        Page<TelegramBotUser> telegramBotUsersPageOne =
                new PageImpl<>(telegramBotUsersForPageOne, pageOnePageable, TOTAL_TELEGRAM_USERS_NUMBER);

        List<TelegramBotUser> telegramBotUsersForPageTwo = LongStream
                .range(PAGE_SIZE, TOTAL_TELEGRAM_USERS_NUMBER)
                .mapToObj(charId -> TelegramBotUser.builder()
                        .chatId(charId)
                        .build()
                )
                .collect(Collectors.toList());

        Pageable pageTwoPageable = PageRequest.of(1, PAGE_SIZE);

        Page<TelegramBotUser> telegramBotUsersPageTwo =
                new PageImpl<>(telegramBotUsersForPageTwo, pageTwoPageable, TOTAL_TELEGRAM_USERS_NUMBER);

        when(telegramBotUserService.findAllByCategory(category, pageOnePageable))
                .thenReturn(telegramBotUsersPageOne);

        when(telegramBotUserService.findAllByCategory(category, pageTwoPageable))
                .thenReturn(telegramBotUsersPageTwo);

        when(freemarkerTemplateResolver.resolve(TEMPLATE_NAME, TEMPLATE_PARAMS))
                .thenReturn(MESSAGE_CONTENT);

        telegramNotificationService.sendNotificationsOfNewPost(post);

        verify(telegramBotUserService).findAllByCategory(category, pageOnePageable);
        verify(telegramBotUserService).findAllByCategory(category, pageTwoPageable);
        verify(freemarkerTemplateResolver, only()).resolve(TEMPLATE_NAME, TEMPLATE_PARAMS);
        LongStream.range(0, TOTAL_TELEGRAM_USERS_NUMBER).forEach(chatId ->
                verify(telegramMessageSender).sendMessageHtml(chatId, MESSAGE_CONTENT)
        );
        verifyNoMoreInteractions(telegramBotUserService, telegramMessageSender);

    }
}