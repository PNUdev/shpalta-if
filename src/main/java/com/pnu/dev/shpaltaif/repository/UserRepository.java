package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
