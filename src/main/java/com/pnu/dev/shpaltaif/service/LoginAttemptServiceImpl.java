package com.pnu.dev.shpaltaif.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pnu.dev.shpaltaif.domain.LoginAttempt;
import com.pnu.dev.shpaltaif.dto.FailedLoginAttemptsInfo;
import com.pnu.dev.shpaltaif.repository.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;

    private final int MAX_ATTEMPT = 10;

    private final LoadingCache<String, Integer> attemptsCache;

    @Autowired
    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository) {

        this.loginAttemptRepository = loginAttemptRepository;
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            public Integer load(String ipAddress) {
                return 0;
            }
        });
    }

    @Override
    public Page<LoginAttempt> findAll(Pageable pageable, boolean blockedOnly) {
        if (blockedOnly) {
            return loginAttemptRepository.findAllByIpBlockedTrue(pageable);
        }
        return loginAttemptRepository.findAll(pageable);
    }

    @Override
    public FailedLoginAttemptsInfo getFailedLoginAttemptsInfo() {
        List<LoginAttempt> failedLoginAttempts = loginAttemptRepository.findAllByIpBlockedTrue();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        List<LoginAttempt> failedLoginAttemptsToday = failedLoginAttempts.stream()
                .filter(loginAttempt -> loginAttempt.getDateTime().isAfter(startOfDay)).collect(Collectors.toList());

        return FailedLoginAttemptsInfo.builder()
                .ipBlockedNumber(failedLoginAttempts.size())
                .ipBlockedNumberToday(failedLoginAttemptsToday.size())
                .build();
    }

    @Override
    public void loginSucceeded(String ipAddress) {
        attemptsCache.invalidate(ipAddress);
        LoginAttempt successLoginAttempt = LoginAttempt.builder()
                .ipAddress(ipAddress)
                .success(true)
                .dateTime(LocalDateTime.now())
                .ipBlocked(false)
                .build();
        loginAttemptRepository.save(successLoginAttempt);
    }

    @Override
    public void loginFailed(String ipAddress) {
        int attempts;
        try {
            attempts = attemptsCache.get(ipAddress);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(ipAddress, attempts);
        LoginAttempt failedLoginAttempt = LoginAttempt.builder()
                .ipAddress(ipAddress)
                .success(false)
                .ipBlocked(isBlocked(ipAddress))
                .dateTime(LocalDateTime.now())
                .build();
        loginAttemptRepository.save(failedLoginAttempt);
    }

    @Override
    public boolean isBlocked(String ipAddress) {
        try {
            return attemptsCache.get(ipAddress) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void deleteOldRecords() {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        loginAttemptRepository.deleteAllByDateTimeBefore(fiveDaysAgo);
    }
}
