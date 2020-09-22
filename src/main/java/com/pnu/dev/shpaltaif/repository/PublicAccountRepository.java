package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicAccountRepository extends JpaRepository<PublicAccount, Long> {

    Optional<PublicAccount> findByUserId(Long userId);

}
