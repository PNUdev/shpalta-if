package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.dto.UpdatePasswordDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, AdminUserInitializer {

    private UserRepository userRepository;

    private PublicAccountService publicAccountService;

    private Environment environment;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PublicAccountService publicAccountService,
                           Environment environment) {

        this.userRepository = userRepository;
        this.publicAccountService = publicAccountService;
        this.environment = environment;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // ToDo implement it along with security
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ServiceAdminException("Користувача не знайдено!"));
    }

    @Transactional
    @Override
    public void create(CreateUserDto createUserDto) {

        if (!StringUtils.equals(createUserDto.getPassword(), createUserDto.getRepeatedPassword())) {
            throw new ServiceAdminException("Паролі не співпадають!");
        }

        if (userRepository.existsByLogin(createUserDto.getLogin())) {
            throw new ServiceAdminException("Логін уже використовується!");
        }

        User user = User.builder()
                .login(createUserDto.getLogin())
                .password(createUserDto.getPassword()) // ToDo password hash should be stored instead
                .role(UserRole.ROLE_WRITER)
                .active(Boolean.TRUE)
                .build();

        User savedUser = userRepository.save(user);

        PublicAccountDto publicAccountDto = PublicAccountDto.builder()
                .name(createUserDto.getName())
                .surname(createUserDto.getSurname())
                .build();

        PublicAccount publicAccount = publicAccountService.create(publicAccountDto, user);

        User userWithPublicAccount = savedUser.toBuilder()
                .publicAccount(publicAccount)
                .build();

        userRepository.save(userWithPublicAccount);

    }

    @Override
    public void updatePassword(User user, UpdatePasswordDto updatePasswordDto) {

        // ToDo implement along with security

        // ToDo compare hash of updatePasswordDto.oldPassword to user.password

        // ToDo compare updatePasswordDto.newPassword and updatePasswordDto.newPasswordRepeated

        // ToDo update user record

    }

    @Override
    public void activate(Long userId) {
        setActive(userId, Boolean.TRUE);
    }

    @Override
    public void deactivate(Long userId) {
        setActive(userId, Boolean.FALSE);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        User user = findById(userId);

        if (user.isActive()) {
            throw new ServiceAdminException("Користувач повинен буте неактивним, щоб його можна було видалити");
        }

        if (user.getRole() == UserRole.ROLE_ADMIN) {
            throw new ServiceAdminException("Неможливо видалити користувача-адміністратора");
        }

        publicAccountService.delete(user.getPublicAccount().getId());
        userRepository.deleteById(user.getId());
    }

    private void setActive(Long userId, boolean active) {

        User user = findById(userId);

        User updatedUser = user.toBuilder()
                .active(active)
                .build();

        userRepository.save(updatedUser);
    }

    @Override
    public void createAdminUserIfNotExists() {

        if (userRepository.existsByRole(UserRole.ROLE_ADMIN)) {
            return;
        }

        User newAdminUser = User.builder()
                .login(environment.getProperty("admin.default.login"))
                .password(environment.getProperty("admin.default.password")) // ToDo hash it
                .role(UserRole.ROLE_ADMIN)
                .active(Boolean.TRUE)
                .build();

        userRepository.save(newAdminUser);

    }
}
