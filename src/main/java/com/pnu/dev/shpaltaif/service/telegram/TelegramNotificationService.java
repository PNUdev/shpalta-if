package com.pnu.dev.shpaltaif.service.telegram;

import com.pnu.dev.shpaltaif.domain.Post;

public interface TelegramNotificationService {

    void sendNotificationsOfNewPost(Post post);

}
