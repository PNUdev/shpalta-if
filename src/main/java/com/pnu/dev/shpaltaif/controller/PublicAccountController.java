package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts")
public class PublicAccountController {

    @GetMapping
    public String findAll(Model model) {

        return "";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable("id") Long id) {

        return "";
    }

    @GetMapping("/edit")
    public String editForm(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {

        return "";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User user, @Validated PublicAccountDto publicAccountDto,
                         RedirectAttributes redirectAttributes) {

        return "";
    }

}
