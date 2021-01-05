package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {

    Page<Feedback> findAll(Pageable pageable);

    long countUnreviewed();

    void create(Feedback feedback);

    void deactivate(Long id);
}
