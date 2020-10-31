package com.pnu.dev.shpaltaif.integration.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.dto.telegram.CategorySubscriptionsInfo;
import com.pnu.dev.shpaltaif.dto.telegram.TelegramSubscriptionsDashboardInfo;
import com.pnu.dev.shpaltaif.dto.telegram.TelegramUserCategorySubscription;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.repository.CategoryRepository;
import com.pnu.dev.shpaltaif.service.telegram.TelegramBotUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TelegramBotUserServiceTest extends BaseIntegrationTest {

    private static final Long CHAT_ID_ONE = 33L;

    private static final Long CHAT_ID_TWO = 35L;

    private static final Integer PREVIOUS_SETTING_MESSAGE_ID = 55;

    private static List<Category> allCategories;

    private static boolean setupDone;

    @Autowired
    private TelegramBotUserService telegramBotUserService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    @Rollback(false)
    public void setup() {

        if (setupDone) {
            return;
        }
        setupDone = true;

        Category categoryOneCreate = Category.builder()
                .title("titleOne")
                .colorTheme("colorThemeOne")
                .publicUrl("publicUrlOne")
                .posts(Collections.emptyList())
                .build();

        Category categoryTwoCreate = Category.builder()
                .title("titleTwo")
                .colorTheme("colorThemeTwo")
                .publicUrl("publicUrlTwo")
                .posts(Collections.emptyList())
                .build();

        allCategories = categoryRepository.saveAll(Arrays.asList(categoryOneCreate, categoryTwoCreate));

    }

    @Test
    @Order(1)
    @Rollback(false)
    public void create() {
        telegramBotUserService.create(CHAT_ID_ONE);
        telegramBotUserService.create(CHAT_ID_TWO);
    }

    @Test
    @Order(2)
    public void findByChatId() {

        TelegramBotUser expectedTelegramUser = TelegramBotUser.builder()
                .chatId(CHAT_ID_ONE)
                .subscribedCategories(allCategories)
                .build();

        TelegramBotUser actualTelegramUser = telegramBotUserService.findByChatId(CHAT_ID_ONE);

        assertEquals(expectedTelegramUser, actualTelegramUser);

    }

    @Test
    @Order(3)
    public void updatePreviousSettingsMessageId() {

        telegramBotUserService.updatePreviousSettingsMessageId(CHAT_ID_ONE, PREVIOUS_SETTING_MESSAGE_ID);

        TelegramBotUser expectedTelegramUser = TelegramBotUser.builder()
                .chatId(CHAT_ID_ONE)
                .previousSettingsMessageId(PREVIOUS_SETTING_MESSAGE_ID)
                .subscribedCategories(allCategories)
                .build();

        TelegramBotUser actualTelegramUser = telegramBotUserService.findByChatId(CHAT_ID_ONE);

        assertEquals(expectedTelegramUser, actualTelegramUser);

    }

    @Test
    public void toggleUserCategorySubscription() {

        TelegramBotUser expectedTelegramUserOneBeforeToggling = TelegramBotUser.builder()
                .chatId(CHAT_ID_ONE)
                .subscribedCategories(allCategories)
                .build();

        TelegramBotUser expectedTelegramUserTwoBeforeToggling = TelegramBotUser.builder()
                .chatId(CHAT_ID_TWO)
                .subscribedCategories(allCategories)
                .build();

        Category categoryToToggle = allCategories.get(0);
        Category categoryToKeep = allCategories.get(1);

        // check that all users will be found by first category
        List<TelegramBotUser> actualTelegramUsersBeforeTogglingByFirstCategory = telegramBotUserService
                .findAllByCategory(categoryToToggle, Pageable.unpaged())
                .toList();

        assertThat(actualTelegramUsersBeforeTogglingByFirstCategory)
                .hasSize(2)
                .contains(expectedTelegramUserOneBeforeToggling)
                .contains(expectedTelegramUserTwoBeforeToggling);

        // check that all users will be found by second category
        List<TelegramBotUser> actualTelegramUsersBeforeTogglingBySecondCategory = telegramBotUserService
                .findAllByCategory(categoryToKeep, Pageable.unpaged())
                .toList();

        assertThat(actualTelegramUsersBeforeTogglingBySecondCategory)
                .hasSize(2)
                .contains(expectedTelegramUserOneBeforeToggling)
                .contains(expectedTelegramUserTwoBeforeToggling);

        // toggle first category
        telegramBotUserService.toggleUserCategorySubscription(CHAT_ID_ONE, categoryToToggle.getId());

        // check that only user that subscribed to the first category will be found (second user)
        List<TelegramBotUser> actualTelegramUsersAfterTogglingByFirstCategory = telegramBotUserService
                .findAllByCategory(categoryToToggle, Pageable.unpaged())
                .toList();

        assertThat(actualTelegramUsersAfterTogglingByFirstCategory)
                .hasSize(1)
                .contains(expectedTelegramUserTwoBeforeToggling);

        // check that all users will be found by second category

        TelegramBotUser expectedTelegramUserOneAfterToggling = TelegramBotUser.builder()
                .chatId(CHAT_ID_ONE)
                .subscribedCategories(Collections.singletonList(categoryToKeep))
                .build();

        List<TelegramBotUser> actualTelegramUsersAfterTogglingBySecondCategory = telegramBotUserService
                .findAllByCategory(categoryToKeep, Pageable.unpaged())
                .toList();

        assertThat(actualTelegramUsersAfterTogglingBySecondCategory)
                .hasSize(2)
                .contains(expectedTelegramUserOneAfterToggling)
                .contains(expectedTelegramUserTwoBeforeToggling);

        // toggle first category again
        telegramBotUserService.toggleUserCategorySubscription(CHAT_ID_ONE, categoryToToggle.getId());

        // check that all users will be found by first category
        List<TelegramBotUser> actualTelegramUsersAfterSecondTogglingByFirstCategory = telegramBotUserService
                .findAllByCategory(categoryToToggle, Pageable.unpaged())
                .toList();

        assertThat(actualTelegramUsersAfterSecondTogglingByFirstCategory)
                .hasSize(2)
                .contains(expectedTelegramUserOneBeforeToggling)
                .contains(expectedTelegramUserTwoBeforeToggling);

        // check that all users will be found by second category
        List<TelegramBotUser> actualTelegramUsersAfterSecondTogglingBySecondCategory = telegramBotUserService
                .findAllByCategory(categoryToKeep, Pageable.unpaged())
                .toList();

        assertThat(actualTelegramUsersAfterSecondTogglingBySecondCategory)
                .hasSize(2)
                .contains(expectedTelegramUserOneBeforeToggling)
                .contains(expectedTelegramUserTwoBeforeToggling);

    }

    @Test
    public void getSubscriptionsDashboardInfo() {

        Category firstCategory = allCategories.get(0);
        Category secondCategory = allCategories.get(1);

        TelegramSubscriptionsDashboardInfo expectedDashboardInfo = TelegramSubscriptionsDashboardInfo.builder()
                .totalUsersCount(2L)
                .subscriptionsInfos(Arrays.asList(
                        CategorySubscriptionsInfo.builder()
                                .category(firstCategory)
                                .subscribedUsersCount(2L)
                                .percentOfTotalUsersCount(100.0)
                                .build(),
                        CategorySubscriptionsInfo.builder()
                                .category(secondCategory)
                                .subscribedUsersCount(1L)
                                .percentOfTotalUsersCount(50.0)
                                .build()
                ))
                .build();

        // toggle second category for one of the users
        telegramBotUserService.toggleUserCategorySubscription(CHAT_ID_TWO, secondCategory.getId());

        TelegramSubscriptionsDashboardInfo actualDashboardInfo = telegramBotUserService.getSubscriptionsDashboardInfo();

        assertThat(actualDashboardInfo).isEqualTo(expectedDashboardInfo);

    }

    @Test
    public void findUserCategorySubscriptions() {

        Category subscribedCategory = allCategories.get(0);
        Category unsubscribedCategory = allCategories.get(1);
        telegramBotUserService.toggleUserCategorySubscription(CHAT_ID_ONE, unsubscribedCategory.getId());

        TelegramUserCategorySubscription expectedTelegramUserCategorySubscriptionOne =
                TelegramUserCategorySubscription.builder()
                        .category(unsubscribedCategory)
                        .subscribed(false)
                        .build();

        TelegramUserCategorySubscription expectedTelegramUserCategorySubscriptionTwo =
                TelegramUserCategorySubscription.builder()
                        .category(subscribedCategory)
                        .subscribed(true)
                        .build();

        List<TelegramUserCategorySubscription> actualUserCategorySubscriptions = telegramBotUserService
                .findUserCategorySubscriptions(CHAT_ID_ONE);

        assertThat(actualUserCategorySubscriptions)
                .hasSize(2)
                .contains(expectedTelegramUserCategorySubscriptionOne)
                .contains(expectedTelegramUserCategorySubscriptionTwo);

    }

}
