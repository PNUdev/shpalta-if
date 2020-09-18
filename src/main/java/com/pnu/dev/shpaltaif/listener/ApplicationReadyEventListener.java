package com.pnu.dev.shpaltaif.listener;

import com.pnu.dev.shpaltaif.service.AdminUserInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private AdminUserInitializer adminUserInitializer;

    @Autowired
    public ApplicationReadyEventListener(AdminUserInitializer adminUserInitializer) {
        this.adminUserInitializer = adminUserInitializer;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        adminUserInitializer.createAdminUserIfNotExists();
    }

}
