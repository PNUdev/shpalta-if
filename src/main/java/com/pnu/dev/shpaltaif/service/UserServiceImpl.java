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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, AdminUserInitializer {

    private UserRepository userRepository;

    private PublicAccountService publicAccountService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PublicAccountService publicAccountService) {

        this.userRepository = userRepository;
        this.publicAccountService = publicAccountService;
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

        Optional<User> adminUser = userRepository.findUserByRole(UserRole.ROLE_ADMIN);

        if (adminUser.isPresent()) {
            return;
        }

        User newAdminUser = User.builder()
                .login("defaultLogin")
                .password("defaultPassword") // ToDo password hash should be stored instead + use env for this values
                .role(UserRole.ROLE_ADMIN)
                .active(Boolean.TRUE)
                .build();

        userRepository.save(newAdminUser);

    }
}
