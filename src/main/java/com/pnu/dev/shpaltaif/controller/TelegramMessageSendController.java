package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Category;
import com.pnu.dev.shpaltaif.dto.TelegramMessage;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.service.CategoryService;
import com.pnu.dev.shpaltaif.service.telegram.TelegramMessageSendService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;
import static java.util.Objects.isNull;

@Controller
@RequestMapping("/admin/telegram-message")
public class TelegramMessageSendController {

    private final CategoryService categoryService;

    private final TelegramMessageSendService telegramMessageSendService;

    public TelegramMessageSendController(CategoryService categoryService,
                                         TelegramMessageSendService telegramMessageSendService) {

        this.categoryService = categoryService;
        this.telegramMessageSendService = telegramMessageSendService;
    }

    @GetMapping
    public String showForm(Model model) {

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);

        return "admin/telegram/messageForm";
    }

    @PostMapping
    public String sendMessage(TelegramMessage telegramMessage, RedirectAttributes redirectAttributes) {

        if (telegramMessage.isShareForAllUsers()) {
            telegramMessageSendService.sendMessageToAllCategories(telegramMessage.getContent());
        } else {
            validateCategoryId(telegramMessage.getCategoryId());
            Category category = categoryService.findById(telegramMessage.getCategoryId());
            telegramMessageSendService.sendMessageToCategory(telegramMessage.getContent(), category);
        }

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Повідомлення було успішно поширено у Телеграмі");

        return "redirect:/admin/telegram-message";
    }

    private void validateCategoryId(Long categoryId) {
        if (isNull(categoryId)) {
            throw new ServiceException("ID категорії має бути вказане");
        }
    }

}
