package com.pnu.dev.shpaltaif.integration.service;

import com.pnu.dev.shpaltaif.domain.Feedback;
import com.pnu.dev.shpaltaif.integration.BaseIntegrationTest;
import com.pnu.dev.shpaltaif.repository.FeedbackRepository;
import com.pnu.dev.shpaltaif.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeedbackServiceIntegrationTest extends BaseIntegrationTest {

    private final static Pageable PAGEABLE = PageRequest.of(0, 10,
            Sort.by(Sort.Direction.ASC, "reviewed").and(Sort.by(Sort.Direction.DESC, "createdAt")));

    private final static String FEEDBACK_CONTENT = "content";

    private final static String FEEDBACK_USER_INFO = "userInfo";


    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    public void createThanReviewThanDeactivateTest() {

        assertEquals(0, feedbackService.countUnreviewed());
        assertEquals(0, feedbackService.findAll(PAGEABLE).getTotalElements());

        //Create feedback
        Feedback feedbackToCreate = Feedback.builder()
                .content(FEEDBACK_CONTENT)
                .userInfo(FEEDBACK_USER_INFO)
                .build();

        feedbackService.create(feedbackToCreate);

        Feedback createdFeedback = feedbackRepository.findAll().get(0);

        Feedback expectedCreatedFeedback = Feedback.builder()
                .content(FEEDBACK_CONTENT)
                .userInfo(FEEDBACK_USER_INFO)
                .reviewed(false)
                .active(true)
                .build();

        //Assert createdFeedback
        assertEquals(1, feedbackService.countUnreviewed());
        assertThat(createdFeedback)
                .isEqualToIgnoringGivenFields(expectedCreatedFeedback, "id", "createdAt");

        //Review created feedback
        Page<Feedback> feedbacksAfterCreate = feedbackService.findAll(PAGEABLE);

        Feedback expectedFeedbackAfterReview = Feedback.builder()
                .content(FEEDBACK_CONTENT)
                .userInfo(FEEDBACK_USER_INFO)
                .reviewed(true)
                .active(true)
                .build();

        //Assertions after review
        assertEquals(1, feedbacksAfterCreate.getTotalElements());
        assertEquals(0, feedbackService.countUnreviewed());
        assertThat(feedbacksAfterCreate.getContent().get(0))
                .isEqualToIgnoringGivenFields(expectedFeedbackAfterReview, "id", "createdAt");

        //Deactivate feedback
        feedbackService.deactivate(createdFeedback.getId());
        Page<Feedback> feedbacksAfterDeactivate = feedbackService.findAll(PAGEABLE);
        assertEquals(0, feedbacksAfterDeactivate.getTotalElements());

    }

    @Test
    public void createManyFeedbacksAndReviewTest() {

        assertEquals(0, feedbackService.countUnreviewed());
        assertEquals(0, feedbackService.findAll(PAGEABLE).getTotalElements());

        //Create 15 feedbacks
        int createFeedbacksNumber = 15;
        Feedback feedback = Feedback.builder()
                .content(FEEDBACK_CONTENT)
                .build();

        IntStream.range(0, createFeedbacksNumber).forEach((i) -> feedbackService.create(feedback));
        assertEquals(createFeedbacksNumber, feedbackService.countUnreviewed());

        //Read 10 last unreviewed feedbacks, 5 unread remains
        Page<Feedback> reviewedFeedbacksFirstTime = feedbackService.findAll(PAGEABLE);
        assertEquals(PAGEABLE.getPageSize(), reviewedFeedbacksFirstTime.getNumberOfElements());
        assertEquals(createFeedbacksNumber - reviewedFeedbacksFirstTime.getNumberOfElements(), feedbackService.countUnreviewed());
        reviewedFeedbacksFirstTime.forEach(f -> assertTrue(f.isReviewed()));

        //Read feedbacks, unreviewed should presented, even thought they were created before others
        Page<Feedback> reviewedFeedbacksSecondTime = feedbackService.findAll(PAGEABLE);
        assertEquals(PAGEABLE.getPageSize(), reviewedFeedbacksSecondTime.getNumberOfElements());
        assertEquals(0, feedbackService.countUnreviewed());

    }
}
