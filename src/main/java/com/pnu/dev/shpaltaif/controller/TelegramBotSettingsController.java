package com.pnu.dev.shpaltaif.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/telegram-bot/settings")
public class TelegramBotSettingsController { // ToDo implement

    @GetMapping("/{settingsToken}")
    public String edit(@PathVariable("settingsToken") String settingsToken) {
        return null;
    }

    @PostMapping("/{settingsToken}")
    public String update(@PathVariable("settingsToken") String settingsToken, RedirectAttributes redirectAttributes) {


        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Ваші підписки було успішно оновлено");

        return "redirect:/telegram-bot/settings/" + settingsToken;
    }

}
