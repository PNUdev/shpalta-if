package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.TelegramBotUser;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.util.FreemarkerTemplateResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class TelegramBot extends TelegramWebhookBot implements SelfRegisteringTelegramBot, TelegramMessageSender {

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
    public void register() {

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

    private SendMessage handleMessage(Long chatId, String message) {

        if (StringUtils.equals(message, "/start")) {
            telegramBotUserService.create(chatId);
            return buildSendMessageHtmlFromTemplate(chatId,
                    "/telegram/start.ftl",
                    Collections.singletonMap("appBasePath", appBasePath));
        }

        if (StringUtils.equals(message, "/settings")) {

            TelegramBotUser telegramBotUser = telegramBotUserService.findByChatId(chatId);

            Map<String, Object> params = new HashMap<>();
            params.put("appBasePath", this.appBasePath);
            params.put("settingsToken", telegramBotUser.getSettingsToken());

            return buildSendMessageHtmlFromTemplate(chatId, "/telegram/settings.ftl", params);
        }

        return buildSendMessageHtmlFromTemplate(chatId, "/telegram/help.ftl", Collections.emptyMap());
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
}
