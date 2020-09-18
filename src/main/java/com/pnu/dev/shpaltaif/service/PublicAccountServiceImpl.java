package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.exception.ServiceAdminException;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublicAccountServiceImpl implements PublicAccountService {

    private PublicAccountRepository publicAccountRepository;

    private PostRepository postRepository;

    @Autowired
    public PublicAccountServiceImpl(PublicAccountRepository publicAccountRepository, PostRepository postRepository) {

        this.publicAccountRepository = publicAccountRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<PublicAccount> findAll() {
        return publicAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "surname"));
    }

    @Override
    public PublicAccount findById(Long accountId) {
        return publicAccountRepository.findById(accountId)
                .orElseThrow(() -> new ServiceAdminException("Акаунт не знайдено"));
    }

    @Override
    @Transactional
    public PublicAccount create(PublicAccountDto publicAccountDto, User user) {

        if (user.getRole() != UserRole.ROLE_WRITER) {
            throw new ServiceAdminException("Тільки дописувачі можуть мати публічні акаунти");
        }

        LocalDateTime now = LocalDateTime.now();

        PublicAccount publicAccount = PublicAccount.builder()
                .name(publicAccountDto.getName())
                .surname(publicAccountDto.getSurname())
                .profileImageUrl(publicAccountDto.getProfileImageUrl())
                .description(publicAccountDto.getDescription())
                .createdAt(now)
                .updatedAt(now)
                .user(user)
                .build();

        return publicAccountRepository.save(publicAccount);
    }

    @Override
    public PublicAccount update(PublicAccountDto publicAccountDto, Long accountId) {

        PublicAccount publicAccount = findById(accountId);

        PublicAccount updatedPublicAccount = publicAccount.toBuilder()
                .name(publicAccountDto.getName())
                .surname(publicAccountDto.getSurname())
                .profileImageUrl(publicAccountDto.getProfileImageUrl())
                .description(publicAccountDto.getDescription())
                .updatedAt(LocalDateTime.now())
                .build();

        return publicAccountRepository.save(updatedPublicAccount);
    }

    @Override
    public void delete(Long accountId) {

        if (postRepository.existsPostsByAuthorPublicAccountId(accountId)) {
            throw new ServiceAdminException("Неможливо видалити акаунт користувача, який має існуючі пости");
        }

        publicAccountRepository.deleteById(accountId);
    }
}
