package com.pnu.dev.shpaltaif.service;


import com.pnu.dev.shpaltaif.domain.HeaderLink;
import com.pnu.dev.shpaltaif.dto.HeaderLinkCreateDto;
import com.pnu.dev.shpaltaif.dto.HeaderLinkUpdateDto;

import java.util.List;

public interface HeaderLinkService {

    List<HeaderLink> findAll();

    HeaderLink findById(Long id);

    HeaderLink create(HeaderLinkCreateDto headerLinkCreateDto);

    HeaderLink update(HeaderLinkUpdateDto headerLinkUpdateDto, Long id);

    void delete(Long id);

    void moveBottom(Long id);

    void moveTop(Long id);

}
