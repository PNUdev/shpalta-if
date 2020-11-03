package com.pnu.dev.shpaltaif.listener;

import com.pnu.dev.shpaltaif.service.AdminUserInitializer;
import com.pnu.dev.shpaltaif.service.telegram.SelfRegisteringTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private AdminUserInitializer adminUserInitializer;

    private SelfRegisteringTelegramBot telegramBot;

    @Autowired
    public ApplicationReadyEventListener(AdminUserInitializer adminUserInitializer,
                                         SelfRegisteringTelegramBot telegramBot) {

        this.adminUserInitializer = adminUserInitializer;
        this.telegramBot = telegramBot;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        adminUserInitializer.createAdminUserIfNotExists();
        telegramBot.registerWebhook();
    }

}
