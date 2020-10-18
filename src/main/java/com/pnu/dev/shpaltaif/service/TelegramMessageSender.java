package com.pnu.dev.shpaltaif.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TelegramMessageSender {

    void sendMessage(SendMessage message);

}
