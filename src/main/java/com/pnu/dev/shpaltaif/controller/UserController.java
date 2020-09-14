package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.dto.UserDto;
import com.pnu.dev.shpaltaif.dto.UserUpdateDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String findAll(Model model) {

        return "";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable("id") Long id) {

        return "";
    }

    @GetMapping("/new")
    public String createForm() {
        return "";
    }

    @GetMapping("/edit/{id}")
    public String editForm(Model model, @PathVariable("id") Long id) {

        return "";
    }

    @PostMapping("/new")
    public String create(@Validated UserDto userDto, RedirectAttributes redirectAttributes) {

        return "";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, UserUpdateDto userUpdateDto) {
        return "";
    }


    @PostMapping("/activate/{id}")
    public String activate(@PathVariable("id") Long id, @Validated UserDto userDto,
                           RedirectAttributes redirectAttributes) {

        return "";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable("id") Long id, @Validated UserDto userDto,
                             RedirectAttributes redirectAttributes) {

        return "";
    }
}
