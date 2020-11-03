package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.PostRepository;
import com.pnu.dev.shpaltaif.repository.PublicAccountRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

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
                .orElseThrow(() -> new ServiceException("Акаунт не знайдено"));
    }

    @Override
    public PublicAccount create(PublicAccountDto publicAccountDto, User user) {

        if (user.getRole() != UserRole.ROLE_WRITER) {
            throw new ServiceException("Тільки дописувачі можуть мати публічні акаунти");
        }

        LocalDateTime now = LocalDateTime.now();

        PublicAccount publicAccount = PublicAccount.builder()
                .name(publicAccountDto.getName())
                .surname(publicAccountDto.getSurname())
                .pseudonymUsed(Boolean.FALSE)
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

        if (nonNull(publicAccountDto.getPseudonym())) {
            publicAccountDto.setPseudonym(publicAccountDto.getPseudonym().trim());
            if (publicAccountRepository.existsByPseudonymAndIdNot(publicAccountDto.getPseudonym(), accountId)) {
                throw new ServiceException("Псевдонім зайнятий");
            }
        }

        if (publicAccountDto.isPseudonymUsed() && StringUtils.isBlank(publicAccountDto.getPseudonym())) {
            throw new ServiceException("Щоб використовувати псевдонім, введіть його коректно");
        }

        PublicAccount publicAccount = findById(accountId);

        PublicAccount updatedPublicAccount = publicAccount.toBuilder()
                .name(publicAccountDto.getName())
                .surname(publicAccountDto.getSurname())
                .pseudonym(publicAccountDto.getPseudonym())
                .pseudonymUsed(publicAccountDto.isPseudonymUsed())
                .profileImageUrl(publicAccountDto.getProfileImageUrl())
                .description(publicAccountDto.getDescription())
                .updatedAt(LocalDateTime.now())
                .build();

        return publicAccountRepository.save(updatedPublicAccount);
    }

    @Override
    public void delete(Long accountId) {

        if (postRepository.existsPostsByAuthorPublicAccountId(accountId)) {
            throw new ServiceException("Неможливо видалити акаунт користувача, який має існуючі пости");
        }

        publicAccountRepository.deleteById(accountId);
    }
}
