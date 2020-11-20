package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.LoginAttempt;
import com.pnu.dev.shpaltaif.dto.FailedLoginAttemptsInfo;
import com.pnu.dev.shpaltaif.repository.LoginAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;

    private final int MAX_ATTEMPTS_NUMBER = 10;

    @Autowired
    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository) {

        this.loginAttemptRepository = loginAttemptRepository;
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
        List<LoginAttempt> failedLoginAttemptsToday =
                loginAttemptRepository.findAllByIpBlockedTrueAndDateTimeAfter(LocalDate.now().atStartOfDay());

        return FailedLoginAttemptsInfo.builder()
                .ipBlockedNumber(failedLoginAttempts.size())
                .ipBlockedNumberToday(failedLoginAttemptsToday.size())
                .build();
    }

    @Override
    public void loginSucceeded(String ipAddress) {
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
        List<LoginAttempt> attemptsForLast24Hours = getLoginAttemptsForLast24Hours(ipAddress);
        int failedAttemptsNumber = 1;
        for (LoginAttempt loginAttempt : attemptsForLast24Hours) {
            if (loginAttempt.isSuccess()) {
                break;
            } else {
                failedAttemptsNumber++;
            }
        }
        boolean ipBlocked = failedAttemptsNumber >= MAX_ATTEMPTS_NUMBER;
        LoginAttempt failedLoginAttempt = LoginAttempt.builder()
                .ipAddress(ipAddress)
                .success(false)
                .ipBlocked(ipBlocked)
                .dateTime(LocalDateTime.now())
                .build();
        loginAttemptRepository.save(failedLoginAttempt);
    }


    @Override
    public boolean isBlocked(String ipAddress) {
        List<LoginAttempt> attemptsForLast24Hours = getLoginAttemptsForLast24Hours(ipAddress);
        return attemptsForLast24Hours.stream().anyMatch(LoginAttempt::isIpBlocked);
    }

    private List<LoginAttempt> getLoginAttemptsForLast24Hours(String ipAddress) {
        LocalDateTime dateTime24HoursAgo = LocalDateTime.now().minusHours(24);
        return loginAttemptRepository
                .findAllByIpAddressEqualsAndDateTimeAfter(ipAddress,
                        dateTime24HoursAgo, Sort.by(Sort.Direction.DESC, "dateTime"));
    }

    @Override
    @Transactional
    public void deleteOldRecords() {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        loginAttemptRepository.deleteAllByDateTimeBefore(fiveDaysAgo);
    }
}
