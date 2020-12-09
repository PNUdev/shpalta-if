package com.pnu.dev.shpaltaif.repository;

import com.pnu.dev.shpaltaif.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Page<Feedback> findAllByActiveTrue(Pageable pageable);

    long countAllByReviewedFalseAndActiveTrue();
}
