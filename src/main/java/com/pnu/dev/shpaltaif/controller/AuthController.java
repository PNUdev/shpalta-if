package com.pnu.dev.shpaltaif.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_ERROR;
import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;
import static java.util.Objects.nonNull;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {

        if (nonNull(error)) {
            String errorMessage = "Помилка авторизації";
            if (error.equalsIgnoreCase("Bad credentials")) {
                errorMessage = "Неправильні ім'я користувача або пароль!";
            } else if (error.equalsIgnoreCase("blocked")) {
                errorMessage = "Забагато невдалих спроб входу, ваша IP-адреса заблокована на 24 години!";
            }
            model.addAttribute(FLASH_MESSAGE_ERROR, errorMessage);
        }

        if (nonNull(logout))
            model.addAttribute(FLASH_MESSAGE_SUCCESS, "Ви успішно вийшли!");

        return "/admin/login";
    }

}
