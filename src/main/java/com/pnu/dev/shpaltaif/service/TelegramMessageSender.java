package com.pnu.dev.shpaltaif.service;

public interface TelegramMessageSender {

    void sendMessageHtml(Long chatId, String content);

}
