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

import java.util.HashMap;
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

    private static final String NEW_POST_TEMPLATE_NAME = "/telegram/newPost.ftl";

    private static final Map<String, Object> NEW_POST_TEMPLATE_PARAMS = new HashMap<String, Object>() {{
        put("postId", POST_ID);
        put("appBasePath", APP_BASE_URL);
    }};

    private static final Category CATEGORY = Category.builder()
            .title("category title")
            .build();

    private static final String NEW_CATEGORY_TEMPLATE_NAME = "/telegram/newCategory.ftl";

    private static final Map<String, Object> NEW_CATEGORY_TEMPLATE_PARAMS = new HashMap<String, Object>() {{
        put("category", CATEGORY);
        put("appBasePath", APP_BASE_URL);
    }};

    private TelegramBotUserService telegramBotUserService = mock(TelegramBotUserService.class);

    private TelegramMessageSender telegramMessageSender = mock(TelegramMessageSender.class);

    private FreemarkerTemplateResolver freemarkerTemplateResolver = mock(FreemarkerTemplateResolver.class);

    private TelegramNotificationService telegramNotificationService = new TelegramNotificationServiceImpl(
            telegramBotUserService, telegramMessageSender, freemarkerTemplateResolver, APP_BASE_URL);

    @Test
    void sendNotificationsOfNewPostSuccessFlow() {

        Post post = buildPost();

        List<TelegramBotUser> telegramBotUsersForPageOne = buildTelegramBotUsersPageOne();

        Pageable pageOnePageable = PageRequest.of(0, PAGE_SIZE);

        Page<TelegramBotUser> telegramBotUsersPageOne =
                new PageImpl<>(telegramBotUsersForPageOne, pageOnePageable, TOTAL_TELEGRAM_USERS_NUMBER);

        List<TelegramBotUser> telegramBotUsersForPageTwo = buildTelegramBotUsersPageTwo();

        Pageable pageTwoPageable = PageRequest.of(1, PAGE_SIZE);

        Page<TelegramBotUser> telegramBotUsersPageTwo =
                new PageImpl<>(telegramBotUsersForPageTwo, pageTwoPageable, TOTAL_TELEGRAM_USERS_NUMBER);

        when(telegramBotUserService.findAllByCategory(CATEGORY, pageOnePageable))
                .thenReturn(telegramBotUsersPageOne);

        when(telegramBotUserService.findAllByCategory(CATEGORY, pageTwoPageable))
                .thenReturn(telegramBotUsersPageTwo);

        when(freemarkerTemplateResolver.resolve(NEW_POST_TEMPLATE_NAME, NEW_POST_TEMPLATE_PARAMS))
                .thenReturn(MESSAGE_CONTENT);

        telegramNotificationService.sendNotificationsOfNewPost(post);

        verify(telegramBotUserService).findAllByCategory(CATEGORY, pageOnePageable);
        verify(telegramBotUserService).findAllByCategory(CATEGORY, pageTwoPageable);
        verify(freemarkerTemplateResolver, only()).resolve(NEW_POST_TEMPLATE_NAME, NEW_POST_TEMPLATE_PARAMS);
        LongStream.range(0, TOTAL_TELEGRAM_USERS_NUMBER).forEach(chatId ->
                verify(telegramMessageSender).sendMessageHtml(chatId, MESSAGE_CONTENT)
        );
        verifyNoMoreInteractions(telegramBotUserService, telegramMessageSender);

    }

    private List<TelegramBotUser> buildTelegramBotUsersPageTwo() {
        return LongStream
                .range(PAGE_SIZE, TOTAL_TELEGRAM_USERS_NUMBER)
                .mapToObj(charId -> TelegramBotUser.builder()
                        .chatId(charId)
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Test
    void sendNotificationsOfNewCategorySuccessFlow() {

        Post post = buildPost();

        List<TelegramBotUser> telegramBotUsersForPageOne = buildTelegramBotUsersPageOne();

        Pageable pageOnePageable = PageRequest.of(0, PAGE_SIZE);

        Page<TelegramBotUser> telegramBotUsersPageOne =
                new PageImpl<>(telegramBotUsersForPageOne, pageOnePageable, TOTAL_TELEGRAM_USERS_NUMBER);

        List<TelegramBotUser> telegramBotUsersForPageTwo = buildTelegramBotUsersPageTwo();

        Pageable pageTwoPageable = PageRequest.of(1, PAGE_SIZE);

        Page<TelegramBotUser> telegramBotUsersPageTwo =
                new PageImpl<>(telegramBotUsersForPageTwo, pageTwoPageable, TOTAL_TELEGRAM_USERS_NUMBER);

        when(telegramBotUserService.findAll(pageOnePageable))
                .thenReturn(telegramBotUsersPageOne);

        when(telegramBotUserService.findAll(pageTwoPageable))
                .thenReturn(telegramBotUsersPageTwo);

        when(freemarkerTemplateResolver.resolve(NEW_CATEGORY_TEMPLATE_NAME, NEW_CATEGORY_TEMPLATE_PARAMS))
                .thenReturn(MESSAGE_CONTENT);

        telegramNotificationService.sendNotificationsOfNewCategory(CATEGORY);

        verify(telegramBotUserService).findAll(pageOnePageable);
        verify(telegramBotUserService).findAll(pageTwoPageable);
        verify(freemarkerTemplateResolver, only()).resolve(NEW_CATEGORY_TEMPLATE_NAME, NEW_CATEGORY_TEMPLATE_PARAMS);
        LongStream.range(0, TOTAL_TELEGRAM_USERS_NUMBER).forEach(chatId ->
                verify(telegramMessageSender).sendMessageHtml(chatId, MESSAGE_CONTENT)
        );
        verifyNoMoreInteractions(telegramBotUserService, telegramMessageSender);

    }

    private List<TelegramBotUser> buildTelegramBotUsersPageOne() {
        return LongStream.range(0, PAGE_SIZE)
                .mapToObj(charId -> TelegramBotUser.builder()
                        .chatId(charId)
                        .build()
                )
                .collect(Collectors.toList());
    }

    private Post buildPost() {
        return Post.builder()
                .id(POST_ID)
                .category(CATEGORY)
                .build();
    }
}