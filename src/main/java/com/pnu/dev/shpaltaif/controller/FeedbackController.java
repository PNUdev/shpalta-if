package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Feedback;
import com.pnu.dev.shpaltaif.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String createForm() {
        return "feedback/form";
    }

    @GetMapping("/after-submit")
    public String showAfterSubmitPage() {

        return "feedback/success";
    }

    @PostMapping("/create")
    public String create(Feedback feedback) {
        feedbackService.create(feedback);
        return "redirect:/feedbacks/after-submit";
    }
}
