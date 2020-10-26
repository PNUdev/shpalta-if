package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.dto.TelegramUserCategorySubscriptions;
import com.pnu.dev.shpaltaif.service.TelegramBotUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/telegram-bot/settings")
public class TelegramBotSettingsController {

    private TelegramBotUserService telegramBotUserService;

    @Autowired
    public TelegramBotSettingsController(TelegramBotUserService telegramBotUserService) {
        this.telegramBotUserService = telegramBotUserService;
    }

    @GetMapping("/{settingsToken}")
    public String edit(@PathVariable("settingsToken") String settingsToken, Model model) {

        TelegramUserCategorySubscriptions subscriptions = telegramBotUserService
                .findUserCategorySubscriptions(settingsToken);

        model.addAttribute("subscriptions", subscriptions);

        return "telegram/settings/edit";
    }

    @PostMapping("/{settingsToken}")
    public String update(@PathVariable("settingsToken") String settingsToken,
                         TelegramUserCategorySubscriptions subscriptions,
                         RedirectAttributes redirectAttributes) {

        telegramBotUserService.updateUserCategorySubscriptions(settingsToken, subscriptions);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Ваші підписки було успішно оновлено");

        return "redirect:/telegram-bot/settings/" + settingsToken;
    }

}
