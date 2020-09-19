package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.CreateUserDto;
import com.pnu.dev.shpaltaif.dto.UpdatePasswordDto;
import com.pnu.dev.shpaltaif.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "/admin/user/index";
    }

    @GetMapping("/new")
    public String createForm() {
        return "";
    }

    @GetMapping("/update-password")
    public String updatePasswordForm(@AuthenticationPrincipal User user) {

        return "";
    }

    @PostMapping("/new")
    public String create(@Validated CreateUserDto createUserDto, RedirectAttributes redirectAttributes) {

        return "";
    }

    @PostMapping("/update-password")
    public String resetPassword(@AuthenticationPrincipal User user, @Validated UpdatePasswordDto updatePasswordDto) {
        return "";
    }


    @PostMapping("/activate/{id}")
    public String activate(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        return "";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        return "";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        return "";
    }
}
