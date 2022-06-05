package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.HeaderLink;
import com.pnu.dev.shpaltaif.dto.HeaderLinkCreateDto;
import com.pnu.dev.shpaltaif.dto.HeaderLinkUpdateDto;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.HeaderLinkRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HeaderLinkServiceImpl implements HeaderLinkService {

    private final HeaderLinkRepository headerLinkRepository;

    public HeaderLinkServiceImpl(HeaderLinkRepository headerLinkRepository) {
        this.headerLinkRepository = headerLinkRepository;
    }


    @Override
    public List<HeaderLink> findAll() {
        return headerLinkRepository.findAll(Sort.by("sequence"));
    }

    @Override
    public HeaderLink findById(Long id) {
        return headerLinkRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Посилання не знайдено"));
    }

    @Override
    public HeaderLink create(HeaderLinkCreateDto headerLinkCreateDto) {
        Long nextSequence = ObjectUtils.firstNonNull(headerLinkRepository.findMaxSequence(), -1L) + 1;

        HeaderLink headerLink = HeaderLink.builder()
                .name(headerLinkCreateDto.getName())
                .link(headerLinkCreateDto.getLink())
                .openInNewTab(headerLinkCreateDto.isOpenInNewTab())
                .sequence(nextSequence)
                .build();

        return headerLinkRepository.save(headerLink);
    }

    @Override
    public HeaderLink update(HeaderLinkUpdateDto headerLinkUpdateDto, Long id) {
        HeaderLink headerLink = findById(id).toBuilder()
                .name(headerLinkUpdateDto.getName())
                .link(headerLinkUpdateDto.getLink())
                .openInNewTab(headerLinkUpdateDto.isOpenInNewTab())
                .build();

        return headerLinkRepository.save(headerLink);
    }

    @Override
    public void delete(Long id) {
        headerLinkRepository.delete(findById(id));
    }

    @Override
    @Transactional
    public void moveBottom(Long id) {
        HeaderLink current = findById(id);
        HeaderLink fromBottom = headerLinkRepository.findFirstBySequenceGreaterThanOrderBySequenceAsc(current.getSequence());
        swapSequenceAndSave(current, fromBottom);
    }

    @Override
    @Transactional
    public void moveTop(Long id) {
        HeaderLink current = findById(id);
        HeaderLink fromTop = headerLinkRepository.findFirstBySequenceLessThanOrderBySequenceDesc(current.getSequence());
        swapSequenceAndSave(current, fromTop);
    }

    private void swapSequenceAndSave(HeaderLink headerLink1, HeaderLink headerLink2) {
        Long sequence2 = headerLink2.getSequence();
        HeaderLink updated2 = headerLink2.toBuilder()
                .sequence(headerLink1.getSequence())
                .build();
        HeaderLink updated1 = headerLink1.toBuilder()
                .sequence(sequence2)
                .build();
        headerLinkRepository.save(updated1);
        headerLinkRepository.save(updated2);
    }

}
