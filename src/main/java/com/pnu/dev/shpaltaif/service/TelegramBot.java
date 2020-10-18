package com.pnu.dev.shpaltaif.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.isNull;

@Service
public class TelegramBot extends TelegramWebhookBot implements SelfRegisteringTelegramBot {

    @Value("${telegrambot.username}")
    private String botUsername;

    @Value("${telegrambot.token}")
    private String botToken;

    @Value("${telegrambot.url.secret}")
    private String urlSecret;

    @Value("${app.base_path}")
    private String appPath;

    private RestTemplate restTemplate;

    @Autowired
    public TelegramBot(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {

        if (isNull(update.getMessage()) || isNull(update.getMessage().getChatId())) {
            return new SendMessage();
        }

        Long chatId = update.getMessage().getChatId();

        return new SendMessage(chatId, "Hello, your chat id is " + chatId);
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
        restTemplate.getForEntity(
                String.format("https://api.telegram.org/bot%s/setWebhook?url=%s", botToken, getBotPath()),
                String.class
        );
    }

}
