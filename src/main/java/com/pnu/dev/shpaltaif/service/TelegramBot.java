package com.pnu.dev.shpaltaif.service;

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
    private String appPath;

    private RestTemplate restTemplate;

    private TelegramBotUserService telegramBotUserService;

    @Autowired
    public TelegramBot(RestTemplate restTemplate, TelegramBotUserService telegramBotUserService) {
        this.restTemplate = restTemplate;
        this.telegramBotUserService = telegramBotUserService;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {

        if (isNull(update.getMessage()) || isNull(update.getMessage().getChatId())) {
            return new SendMessage();
        }

        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();

        if (StringUtils.equals(message, "/start")) { // ToDo maybe, have to be rewritten using command
            telegramBotUserService.create(chatId);
            return buildSendMessageHtml(chatId, "Привіт, Ви стали користувачем нашого бота!");
        }

        if (StringUtils.equals(message, "/settings")) {
            return buildSendMessageHtml(chatId, "Посилання на сторінку з налаштуваннями бота");
        }

        if (StringUtils.equals(message, "/help")) {
            return buildSendMessageHtml(chatId, "Повідомлення з доступними командами");
        }

        return buildSendMessageHtml(chatId, "Повідомлення з доступними командами і описом бота");
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
        return appPath + "/telegram-bot-webhook-" + urlSecret;
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

    private SendMessage buildSendMessageHtml(Long chatId, String content) {
        SendMessage sendMessage = new SendMessage(chatId, content);
        sendMessage.enableHtml(true);
        sendMessage.enableWebPagePreview();
        return sendMessage;
    }
}
