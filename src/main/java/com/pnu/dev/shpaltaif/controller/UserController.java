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

// ToDo only ADMIN user should have access to this endpoints (except, update-password)
@Controller
@RequestMapping("/users")
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

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable("id") Long id) {

        return "";
    }

    @GetMapping("/new")
    public String createForm() {
        return "";
    }

    // ToDo this endpoint have to be accessible for any user
    @GetMapping("/update-password/{id}")
    public String updatePasswordForm(Model model, @PathVariable("id") Long id) {

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
