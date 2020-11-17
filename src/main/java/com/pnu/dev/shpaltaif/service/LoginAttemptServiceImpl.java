package com.pnu.dev.shpaltaif.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pnu.dev.shpaltaif.domain.LoginAttempt;
import com.pnu.dev.shpaltaif.repository.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;

    private final int MAX_ATTEMPT = 2;

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
    public Page<LoginAttempt> findAll(Pageable pageable) {
        return loginAttemptRepository.findAll(pageable);
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
}
