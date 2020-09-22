package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;

import java.util.List;

public interface PublicAccountService {

    List<PublicAccount> findAll();

    PublicAccount findById(Long accountId);

    PublicAccount create(PublicAccountDto publicAccountDto, User user);

    PublicAccount update(PublicAccountDto publicAccountDto, Long accountId);

    void delete(Long accountId);

}
