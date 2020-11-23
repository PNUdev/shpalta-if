package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.exception.AuthExceptionMessage;
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
            model.addAttribute(FLASH_MESSAGE_ERROR, AuthExceptionMessage.translateMessage(error));
        }

        if (nonNull(logout))
            model.addAttribute(FLASH_MESSAGE_SUCCESS, "Ви успішно вийшли!");

        return "/admin/login";
    }

}
