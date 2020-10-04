package com.pnu.dev.shpaltaif.aop;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
public class ModelAttributeSetter {

    private CategoryService categoryService;

    @Autowired
    public ModelAttributeSetter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute
    public void setCategories(HttpServletRequest request, Model model) {

        if (
                (!StringUtils.startsWith(request.getRequestURI(), "/admin")
                        || StringUtils.equals(request.getRequestURI(), "/admin/posts")
                        || StringUtils.equals(request.getRequestURI(), "/admin/posts/new")
                        || StringUtils.startsWith(request.getRequestURI(), "/admin/posts/edit")
                )
                        && StringUtils.equals(request.getMethod(), "GET")) {

            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
        }

    }

}
