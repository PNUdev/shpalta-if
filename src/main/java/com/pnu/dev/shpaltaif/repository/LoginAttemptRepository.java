package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.LoginAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    List<LoginAttempt> findAllByIpBlockedTrue();

    List<LoginAttempt> findAllByIpBlockedTrueAndDateTimeAfter(LocalDateTime dateTime);

    List<LoginAttempt> findAllByIpAddressEqualsAndDateTimeAfter(String ip, LocalDateTime dateTime, Sort sort);

    Page<LoginAttempt> findAllByIpBlockedTrue(Pageable pageable);

    void deleteAllByDateTimeBefore(LocalDateTime dateTime);

}
