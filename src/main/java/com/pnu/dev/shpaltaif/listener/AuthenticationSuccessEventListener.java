package com.pnu.dev.shpaltaif.listener;

import com.pnu.dev.shpaltaif.service.LoginAttemptServiceImpl;
import com.pnu.dev.shpaltaif.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginAttemptServiceImpl loginAttemptService;

    @Value("${security-audit-enabled}")
    private boolean securityAuditEnabled;

    @Autowired
    public AuthenticationSuccessEventListener(LoginAttemptServiceImpl loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        if (securityAuditEnabled) {
            String clientIp = HttpUtils.getClientIP();
            loginAttemptService.loginSucceeded(clientIp);
        }
    }
}
