package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.LoginAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    List<LoginAttempt> findAllByIpBlockedTrue();

    Page<LoginAttempt> findAllByIpBlockedTrue(Pageable pageable);

}
