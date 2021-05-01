package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Category;

public interface TelegramMessageSendService {

    void sendMessageToCategory(String messageContent, Category category);

    void sendMessageToAllCategories(String messageContent);

}
