package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.CategoryDto;
import com.pnu.dev.shpaltaif.dto.PostDto;
import com.pnu.dev.shpaltaif.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/admin/posts")
public class PostAdminController {

    private CategoryService categoryService;

    @Autowired
    public PostAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public String findAll(@AuthenticationPrincipal User user, Model model) {

        return "admin/post/index";
    }

    @GetMapping("/new")
    public String createForm(Model model) {

        List categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/post/form";
    }

    @PostMapping("/new")
    public String create(@Validated PostDto postDto) {
        System.out.println(postDto.getContent());
        return "redirect:/admin/categories";
    }
}
