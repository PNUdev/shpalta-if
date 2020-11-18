package com.pnu.dev.shpaltaif.scheduled;

import com.pnu.dev.shpaltaif.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClearLoginAttemptsTable {

    private final LoginAttemptService loginAttemptService;

    @Autowired
    public ClearLoginAttemptsTable(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Kiev")
    public void clearOldLoginAttemptRecords() {
        log.info("Removing old login attempt records from database");
        loginAttemptService.deleteOldRecords();
        log.info("Old login attempt records deleted");
    }
}
