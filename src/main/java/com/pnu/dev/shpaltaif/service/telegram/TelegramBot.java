package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.dto.telegram.TelegramUserCategorySubscription;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class TelegramBot extends TelegramWebhookBot implements SelfRegisteringTelegramBot, TelegramMessageSender {

    private static final String SELECTED_CATEGORY = EmojiParser.parseToUnicode(":white_check_mark:");

    private static final String UNSELECTED_CATEGORY = EmojiParser.parseToUnicode(":white_medium_square:");

    private static final String CATEGORY_CHECKBOX_CALLBACK_ACTION = "category-checkbox";

    private static final String START_COMMAND = "/start";

    private static final String SETTINGS_COMMAND = "/settings";

    @Value("${telegrambot.username}")
    private String botUsername;

    @Value("${telegrambot.token}")
    private String botToken;

    @Value("${telegrambot.url.secret}")
    private String urlSecret;

    @Value("${app.base_path}")
    private String appBasePath;

    private RestTemplate restTemplate;

    private TelegramBotUserService telegramBotUserService;

    private FreemarkerTemplateResolver freemarkerTemplateResolver;

    @Autowired
    public TelegramBot(RestTemplate restTemplate,
                       TelegramBotUserService telegramBotUserService,
                       FreemarkerTemplateResolver freemarkerTemplateResolver) {

        this.restTemplate = restTemplate;
        this.telegramBotUserService = telegramBotUserService;
        this.freemarkerTemplateResolver = freemarkerTemplateResolver;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            return handleCallbackQuery(update.getCallbackQuery());
        }

        if (isNull(update.getMessage()) || isNull(update.getMessage().getChatId())) {
            return new SendMessage();
        }

        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();

        try {
            return handleMessage(chatId, message);
        } catch (ServiceException e) {
            log.error("Error while handling telegram message", e);
            return new SendMessage(chatId, "Помилка: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error while handling telegram message", e);
            return new SendMessage(chatId, "Внутрішня помилка сервера");
        }
    }

    private BotApiMethod handleCallbackQuery(CallbackQuery callbackQuery) {

        String callbackQueryData = callbackQuery.getData();

        Long chatId = callbackQuery.getMessage().getChatId();

        String[] callbackQueryDataParts = callbackQueryData.split(":");

        String action = callbackQueryDataParts[0];
        String data = callbackQueryDataParts[1];

        if (StringUtils.equals(action, CATEGORY_CHECKBOX_CALLBACK_ACTION)) {

            return handleCategoryCheckboxAction(callbackQuery, chatId, data);
        }

        return new SendMessage();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return appBasePath + "/telegram-bot-webhook-" + urlSecret;
    }

    @Override
    public void registerWebhook() {

        log.info("Telegram bot webhook was registered");

        restTemplate.getForEntity(
                String.format("https://api.telegram.org/bot%s/setWebhook?url=%s", botToken, getBotPath()),
                String.class
        );
    }

    @Override
    public void sendMessageHtml(Long chatId, String content) {
        try {
            execute(buildSendMessageHtml(chatId, content));
        } catch (TelegramApiException e) {
            log.error("Error while sending message to telegram", e);
        }
    }

    private BotApiMethod handleCategoryCheckboxAction(CallbackQuery callbackQuery, Long chatId, String data) {

        if (isNotOutdated(callbackQuery.getMessage())) {
            Long categoryId = Long.parseLong(data);
            telegramBotUserService.toggleUserCategorySubscription(chatId, categoryId);
        }

        try {
            return handleMessage(chatId, SETTINGS_COMMAND);
        } catch (Exception e) {
            log.error("Error while handling telegram message", e);
            return new SendMessage(chatId, "Внутрішня помилка сервера");
        }
    }

    private SendMessage handleMessage(Long chatId, String message) throws TelegramApiException {

        if (StringUtils.equals(message, START_COMMAND)) {
            telegramBotUserService.create(chatId);
            return buildSendMessageHtmlFromTemplate(chatId,
                    "/telegram/start.ftl",
                    Collections.singletonMap("appBasePath", appBasePath));
        }

        if (StringUtils.equals(message, SETTINGS_COMMAND)) {

            SendMessage sendMessage = buildSendMessageHtmlFromTemplate(chatId,
                    "/telegram/settings.ftl", Collections.emptyMap());

            sendMessage.setReplyMarkup(buildCategoriesCheckboxListMarkup(chatId));

            TelegramBotUser telegramBotUser = telegramBotUserService.findByChatId(chatId);
            Integer previousSettingsMessageId = telegramBotUser.getPreviousSettingsMessageId();

            if (nonNull(previousSettingsMessageId)) {
                try {
                    execute(new DeleteMessage(chatId, previousSettingsMessageId));
                } catch (TelegramApiRequestException e) {
                    // message cannot be deleted, because more than 48 hours passed since it was sent
                }
            }

            Integer currentMessageId = execute(sendMessage).getMessageId();
            telegramBotUserService.updatePreviousSettingsMessageId(chatId, currentMessageId);

            return new SendMessage();
        }

        return buildSendMessageHtmlFromTemplate(chatId, "/telegram/help.ftl", Collections.emptyMap());
    }

    private InlineKeyboardMarkup buildCategoriesCheckboxListMarkup(Long chatId) {

        List<TelegramUserCategorySubscription> userCategorySubscriptions = telegramBotUserService
                .findUserCategorySubscriptions(chatId);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtons = userCategorySubscriptions.stream()
                .map(subscription -> {

                    Category category = subscription.getCategory();

                    InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
                    keyboardButton.setText(
                            String.join(StringUtils.SPACE,
                                    subscription.isSubscribed() ? SELECTED_CATEGORY : UNSELECTED_CATEGORY,
                                    category.getTitle()
                            )
                    );

                    keyboardButton.setCallbackData(CATEGORY_CHECKBOX_CALLBACK_ACTION + ":" + category.getId());

                    return Collections.singletonList(keyboardButton);
                })
                .collect(Collectors.toList());

        keyboardMarkup.setKeyboard(keyboardButtons);
        return keyboardMarkup;
    }

    private SendMessage buildSendMessageHtmlFromTemplate(Long chatId, String templateName, Map<String, Object> params) {
        String content = freemarkerTemplateResolver.resolve(templateName, params);
        return buildSendMessageHtml(chatId, content);
    }

    private SendMessage buildSendMessageHtml(Long chatId, String content) {
        SendMessage sendMessage = new SendMessage(chatId, content);
        sendMessage.enableHtml(true);
        sendMessage.enableWebPagePreview();
        return sendMessage;
    }

    private boolean isNotOutdated(Message message) {
        LocalDateTime sentTime = LocalDateTime
                .ofInstant(Instant.ofEpochSecond(message.getDate()), ZoneId.of("Europe/Kiev"));

        return sentTime.plusHours(48).isAfter(LocalDateTime.now());
    }
}
