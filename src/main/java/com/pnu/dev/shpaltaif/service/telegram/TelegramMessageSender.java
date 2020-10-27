package com.pnu.dev.shpaltaif.service.telegram;

public interface TelegramMessageSender {

    void sendMessageHtml(Long chatId, String content);

}
