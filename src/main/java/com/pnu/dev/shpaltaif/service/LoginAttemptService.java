package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.LoginAttempt;
import com.pnu.dev.shpaltaif.dto.FailedLoginAttemptsInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoginAttemptService {

    Page<LoginAttempt> findAll(Pageable pageable, boolean blockedOnly);

    FailedLoginAttemptsInfo getFailedLoginAttemptsInfo();

    void loginSucceeded(String key);

    void loginFailed(String key);

    boolean isBlocked(String key);
}
