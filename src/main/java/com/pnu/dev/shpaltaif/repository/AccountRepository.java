package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
