package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.dto.CategoryDto;
import com.pnu.dev.shpaltaif.service.CategoryService;
import com.pnu.dev.shpaltaif.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private CategoryService categoryService;

    @Autowired
    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String findAll(Model model) {

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "admin/category/index";
    }

    @GetMapping("/new")
    public String createForm() {
        return "admin/category/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(Model model, @PathVariable("id") Long id) {

        Category category = categoryService.findById(id);
        model.addAttribute("category", category);

        return "admin/category/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirmation(Model model, @PathVariable("id") Long id, HttpServletRequest request) {

        model.addAttribute("message", "Ви впевнені, що справді хочете видалити категорію?");
        model.addAttribute("returnBackUrl", HttpUtils.getPreviousPageUrl(request));
        model.addAttribute("actionUrl", "/admin/categories/delete/" + id);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/new")
    public String create(@Validated CategoryDto categoryDto, RedirectAttributes redirectAttributes) {

        categoryService.create(categoryDto);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Категорію успішно створено!");

        return "redirect:/admin/categories";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Validated CategoryDto categoryDto,
                         RedirectAttributes redirectAttributes) {

        categoryService.update(id, categoryDto);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Категорію успішно оновлено!");

        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        categoryService.deleteById(id);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Категорію успішно видалено!");

        return "redirect:/admin/categories";
    }
}
