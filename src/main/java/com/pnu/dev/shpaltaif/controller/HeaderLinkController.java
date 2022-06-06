package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.HeaderLink;
import com.pnu.dev.shpaltaif.dto.HeaderLinkCreateDto;
import com.pnu.dev.shpaltaif.dto.HeaderLinkUpdateDto;
import com.pnu.dev.shpaltaif.service.HeaderLinkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/admin/header-link")
public class HeaderLinkController {

    private final HeaderLinkService headerLinkService;

    public HeaderLinkController(HeaderLinkService headerLinkService) {
        this.headerLinkService = headerLinkService;
    }


    @GetMapping
    public String findAll(Model model) {
        List<HeaderLink> headerLinks = headerLinkService.findAll();
        model.addAttribute("headerLinks", headerLinks);
        return "admin/headerLink/index";
    }

    @GetMapping("/new")
    public String createForm() {
        return "admin/headerLink/form";
    }

    @PostMapping("/new")
    public String create(@Valid HeaderLinkCreateDto headerLinkCreateDto, RedirectAttributes redirectAttributes) {
        headerLinkService.create(headerLinkCreateDto);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Посилання успішно створено!");
        return "redirect:/admin/header-link";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        HeaderLink headerLink = headerLinkService.findById(id);
        model.addAttribute("headerLink", headerLink);
        return "admin/headerLink/form";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid HeaderLinkUpdateDto headerLinkUpdateDto, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        headerLinkService.update(headerLinkUpdateDto, id);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Посилання успішно оновлено!");
        return "redirect:/admin/header-link";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        headerLinkService.delete(id);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Посилання успішно видалено!");
        return "redirect:/admin/header-link";
    }

    @PostMapping("/move-bottom/{id}")
    public String moveBottom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        headerLinkService.moveBottom(id);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Посилання успішно переміщено!");
        return "redirect:/admin/header-link";
    }

    @PostMapping("/move-top/{id}")
    public String moveTop(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        headerLinkService.moveTop(id);
        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Посилання успішно переміщено!");
        return "redirect:/admin/header-link";
    }

}
