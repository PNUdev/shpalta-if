package com.pnu.dev.shpaltaif.aop;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.domain.UserRole;
import com.pnu.dev.shpaltaif.service.CategoryService;
import com.pnu.dev.shpaltaif.service.FeedbackService;
import com.pnu.dev.shpaltaif.service.HeaderLinkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class ModelAttributeSetter {

    private CategoryService categoryService;

    private FeedbackService feedbackService;

    private HeaderLinkService headerLinkService;

    @Autowired
    public ModelAttributeSetter(
            CategoryService categoryService,
            FeedbackService feedbackService,
            HeaderLinkService headerLinkService
    ) {
        this.categoryService = categoryService;
        this.feedbackService = feedbackService;
        this.headerLinkService = headerLinkService;
    }

    @ModelAttribute
    public void setCategories(HttpServletRequest request, Model model) {

        if (!StringUtils.startsWith(request.getRequestURI(), "/admin")
                && StringUtils.equals(request.getMethod(), "GET")) {

            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
        }

    }

    @ModelAttribute
    public void setUnreviewedFeedbacksCount(@AuthenticationPrincipal User user, Model model) {
        if (Objects.nonNull(user) && user.getRole() == UserRole.ROLE_ADMIN) {
            model.addAttribute("unreviewedFeedbacksCount", feedbackService.countUnreviewed());
        }
    }

    @ModelAttribute
    public void setHeaderLinks(HttpServletRequest request, Model model) {
        if (!StringUtils.startsWith(request.getRequestURI(), "/admin")
                && StringUtils.equals(request.getMethod(), "GET")) {
            model.addAttribute("headerLinks", headerLinkService.findAll());
        }
    }

}
