package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.dto.UpdatePasswordDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    User findById(Long userId);

    void create(CreateUserDto createUserDto);

    void updatePassword(User user, UpdatePasswordDto updatePasswordDto);

    void activate(Long userId);

    void deactivate(Long userId);

    void delete(Long userId);

}
