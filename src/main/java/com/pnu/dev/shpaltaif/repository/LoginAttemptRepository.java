package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
}
