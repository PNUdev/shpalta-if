package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.dto.AccountDto;
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
public class AccountController {

    @GetMapping
    public String findAll(Model model) {

        return "";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable("id") Long id) {

        return "";
    }

    @GetMapping("/edit/{id}")
    public String editForm(Model model, @PathVariable("id") Long id) {

        return "";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Validated AccountDto accountDto,
                         RedirectAttributes redirectAttributes) {

        return "";
    }

}
