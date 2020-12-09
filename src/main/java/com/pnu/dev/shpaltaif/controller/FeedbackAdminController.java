package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Feedback;
import com.pnu.dev.shpaltaif.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/admin/feedbacks")
public class FeedbackAdminController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackAdminController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String findAll(Model model,
                          @PageableDefault(size = 10)
                          @SortDefault.SortDefaults({
                                  @SortDefault(sort = "reviewed", direction = Sort.Direction.ASC),
                                  @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                          })
                                  Pageable pageable) {
        Page<Feedback> feedbacks = feedbackService.findAll(pageable);
        model.addAttribute("feedbacks", feedbacks);
        return "admin/feedback/index";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        feedbackService.deactivate(id);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Відгук видалено");

        return "redirect:/admin/feedbacks";
    }
}
