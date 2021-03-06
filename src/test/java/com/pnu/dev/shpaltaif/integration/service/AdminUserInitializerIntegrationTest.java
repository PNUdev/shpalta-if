package com.pnu.dev.shpaltaif.integration.service;

import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminUserInitializerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void defaultUserWasCreated() {

        User expectedAdminUser = User.builder()
                .username("admin")
                .password("admin")
                .role(UserRole.ROLE_ADMIN)
                .active(Boolean.TRUE)
                .build();

        List<User> users = userRepository.findAll();

        assertEquals(1, users.size());

        User actualAdminUser = users.get(0);

        assertThat(actualAdminUser).isEqualToIgnoringGivenFields(expectedAdminUser, "id", "password");
        assertTrue(bCryptPasswordEncoder.matches(expectedAdminUser.getPassword(), actualAdminUser.getPassword()));
    }

}
