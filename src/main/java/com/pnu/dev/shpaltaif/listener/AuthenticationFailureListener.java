package com.pnu.dev.shpaltaif.listener;

import com.pnu.dev.shpaltaif.service.LoginAttemptServiceImpl;
import com.pnu.dev.shpaltaif.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptServiceImpl loginAttemptService;

    @Autowired
    public AuthenticationFailureListener(LoginAttemptServiceImpl loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        String clientIp = HttpUtils.getClientIP();
        loginAttemptService.loginFailed(clientIp);
    }

}
