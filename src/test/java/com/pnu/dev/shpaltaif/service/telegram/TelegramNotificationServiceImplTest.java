package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TelegramNotificationServiceImplTest {

    private static final String APP_BASE_URL = "appBaseUrl";

    private static final Long POST_ID = 33L;

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

    private TelegramMessageSendService telegramMessageSendService = mock(TelegramMessageSendService.class);

    private FreemarkerTemplateResolver freemarkerTemplateResolver = mock(FreemarkerTemplateResolver.class);

    private TelegramNotificationService telegramNotificationService = new TelegramNotificationServiceImpl(
            freemarkerTemplateResolver, telegramMessageSendService, APP_BASE_URL);

    @Test
    void sendNotificationsOfNewPostSuccessFlow() {

        Post post = buildPost();

        when(freemarkerTemplateResolver.resolve(NEW_POST_TEMPLATE_NAME, NEW_POST_TEMPLATE_PARAMS))
                .thenReturn(MESSAGE_CONTENT);

        telegramNotificationService.sendNotificationsOfNewPost(post);

        verify(freemarkerTemplateResolver, only()).resolve(NEW_POST_TEMPLATE_NAME, NEW_POST_TEMPLATE_PARAMS);
        verify(telegramMessageSendService, only()).sendMessageToCategory(MESSAGE_CONTENT, CATEGORY);

    }

    @Test
    void sendNotificationsOfNewCategorySuccessFlow() {

        when(freemarkerTemplateResolver.resolve(NEW_CATEGORY_TEMPLATE_NAME, NEW_CATEGORY_TEMPLATE_PARAMS))
                .thenReturn(MESSAGE_CONTENT);

        telegramNotificationService.sendNotificationsOfNewCategory(CATEGORY);

        verify(freemarkerTemplateResolver, only()).resolve(NEW_CATEGORY_TEMPLATE_NAME, NEW_CATEGORY_TEMPLATE_PARAMS);
        verify(telegramMessageSendService, only()).sendMessageToCategory(MESSAGE_CONTENT, CATEGORY);

    }

    private Post buildPost() {
        return Post.builder()
                .id(POST_ID)
                .category(CATEGORY)
                .build();
    }
}