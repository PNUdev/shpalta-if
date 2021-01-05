package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Feedback;
import com.pnu.dev.shpaltaif.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/feedbacks")
public class FeedbackController {

    private static final String SHOW_AFTER_SUBMITTED_ATTRIBUTE = "show-after-submitted";

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
    public String showAfterSubmitPage(Model model) {
        if (model.containsAttribute(SHOW_AFTER_SUBMITTED_ATTRIBUTE)) {
            return "feedback/after-submit";
        }
        return "redirect:/";
    }

    @PostMapping("/create")
    public String create(Feedback feedback, RedirectAttributes redirectAttributes) {
        feedbackService.create(feedback);
        redirectAttributes.addFlashAttribute(SHOW_AFTER_SUBMITTED_ATTRIBUTE, true);
        return "redirect:/feedbacks/after-submit";
    }
}
