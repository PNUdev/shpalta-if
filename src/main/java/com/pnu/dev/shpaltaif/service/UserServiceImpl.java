package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.dto.UpdatePasswordDto;
import com.pnu.dev.shpaltaif.exception.AuthExceptionMessage;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.UserRepository;
import com.pnu.dev.shpaltaif.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class UserServiceImpl implements UserService, AdminUserInitializer, UserDetailsService, AuthSessionSynchronizer {

    private final UserRepository userRepository;

    private final PublicAccountService publicAccountService;

    private final LoginAttemptServiceImpl loginAttemptService;

    private final Environment environment;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PublicAccountService publicAccountService,
                           LoginAttemptServiceImpl loginAttemptService,
                           Environment environment,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.publicAccountService = publicAccountService;
        this.loginAttemptService = loginAttemptService;
        this.environment = environment;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String ip = HttpUtils.getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new InternalAuthenticationServiceException(AuthExceptionMessage.IP_BLOCKED.getValue());
        }
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("Користувача не знайдено!"));
    }

    @Transactional
    @Override
    public void create(CreateUserDto createUserDto) {

        if (!StringUtils.equals(createUserDto.getPassword(), createUserDto.getRepeatedPassword())) {
            throw new ServiceException("Паролі не співпадають!");
        }

        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new ServiceException("Логін уже використовується!");
        }
        if (createUserDto.getRole() == (UserRole.ROLE_WRITER)) {
            validatePublicAccountInfo(createUserDto);
        }

        User user = User.builder()
                .username(createUserDto.getUsername())
                .password(bCryptPasswordEncoder.encode(createUserDto.getPassword()))
                .role(createUserDto.getRole())
                .active(Boolean.TRUE)
                .build();

        User savedUser = userRepository.save(user);

        if (savedUser.getRole() == (UserRole.ROLE_WRITER)) {
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
    }

    @Override
    public void updatePassword(User user, UpdatePasswordDto updatePasswordDto) {

        if (!bCryptPasswordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())) {
            throw new ServiceException("Неправильний старий пароль");
        }

        if (!StringUtils.equals(updatePasswordDto.getNewPassword(), updatePasswordDto.getNewPasswordRepeated())) {
            throw new ServiceException("Паролі не співпадають!");
        }

        User updatedUser = user.toBuilder()
                .password(bCryptPasswordEncoder.encode(updatePasswordDto.getNewPassword()))
                .build();

        userRepository.save(updatedUser);

    }

    @Override
    public void activate(Long userId) {
        User user = findById(userId);
        setActive(user, Boolean.TRUE);
    }

    @Override
    public void deactivate(Long userId) {

        User user = findById(userId);

        if (user.getRole() == UserRole.ROLE_ADMIN) {
            throw new ServiceException("Неможливо деактивувати користувача-адміністратора");
        }

        setActive(user, Boolean.FALSE);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        User user = findById(userId);

        if (user.isActive()) {
            throw new ServiceException("Користувач повинен бути неактивним, щоб його можна було видалити");
        }

        if (user.getRole() == UserRole.ROLE_ADMIN) {
            throw new ServiceException("Неможливо видалити користувача-адміністратора");
        }

        publicAccountService.delete(user.getPublicAccount().getId());
        userRepository.deleteById(user.getId());
    }

    @Override
    public void refreshPrincipalInAuthSession(Long userId) {

        User user = findById(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Override
    public void createAdminUserIfNotExists() {

        if (userRepository.existsByRole(UserRole.ROLE_ADMIN)) {
            return;
        }

        User newAdminUser = User.builder()
                .username(environment.getProperty("admin.default.username"))
                .password(bCryptPasswordEncoder.encode(
                        Objects.requireNonNull(environment.getProperty("admin.default.password")))
                )
                .role(UserRole.ROLE_ADMIN)
                .active(Boolean.TRUE)
                .build();

        userRepository.save(newAdminUser);

    }

    private void validatePublicAccountInfo(CreateUserDto createUserDto) {
        if (isBlank(createUserDto.getName())) {
            throw new ServiceException("Ім'я повинно бути вказаним");
        }
        if (isBlank(createUserDto.getSurname())) {
            throw new ServiceException("Прізвище повинно бути вказаним");
        }
    }

    private void setActive(User user, boolean active) {

        User updatedUser = user.toBuilder()
                .active(active)
                .build();

        userRepository.save(updatedUser);
    }
}
