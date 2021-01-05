package com.pnu.dev.shpaltaif.service;

import com.pnu.dev.shpaltaif.domain.Feedback;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.repository.FeedbackRepository;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Page<Feedback> findAll(Pageable pageable) {
        Page<Feedback> feedbacksPage = feedbackRepository.findAllByActiveTrue(pageable);
        List<Feedback> feedbacksToUpdate = feedbacksPage.getContent().stream()
                .filter(feedback -> !feedback.isReviewed())
                .collect(Collectors.toList());
        markAllAsReviewed(feedbacksToUpdate);
        return feedbacksPage;
    }

    @Override
    public long countUnreviewed() {
        return feedbackRepository.countAllByReviewedFalseAndActiveTrue();
    }

    @Override
    public void create(Feedback feedback) {

        Feedback feedbackToCreate = Feedback.builder()
                .content(StringEscapeUtils.escapeHtml4(feedback.getContent()))
                .reviewed(false)
                .active(true)
                .userInfo(StringEscapeUtils.escapeHtml4(feedback.getUserInfo()))
                .createdAt(LocalDateTime.now())
                .build();

        feedbackRepository.save(feedbackToCreate);
    }

    @Override
    public void deactivate(Long id) {

        Feedback feedbackFromDb = feedbackRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Відгук не знайдено!"));
        Feedback deactivatedFeedback = feedbackFromDb.toBuilder()
                .active(false)
                .build();

        feedbackRepository.save(deactivatedFeedback);
    }

    private void markAllAsReviewed(List<Feedback> feedbacks) {
        List<Feedback> reviewedFeedbacks = feedbacks.stream().map(feedback -> feedback.toBuilder()
                .reviewed(true)
                .build()
        ).collect(Collectors.toList());

        feedbackRepository.saveAll(reviewedFeedbacks);
    }

}
