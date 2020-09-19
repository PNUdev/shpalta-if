package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByRole(UserRole userRole);

    boolean existsByUsername(String login);

    Optional<User> findUserByUsername(String username);

}
