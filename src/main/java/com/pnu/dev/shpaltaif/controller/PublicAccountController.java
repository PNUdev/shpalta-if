package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.PublicAccountDto;
import com.pnu.dev.shpaltaif.service.AuthSessionSynchronizer;
import com.pnu.dev.shpaltaif.service.PublicAccountService;
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

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/accounts")
public class PublicAccountController {

    private PublicAccountService publicAccountService;

    private AuthSessionSynchronizer authSessionSynchronizer;

    @Autowired
    public PublicAccountController(PublicAccountService publicAccountService,
                                   AuthSessionSynchronizer authSessionSynchronizer) {

        this.publicAccountService = publicAccountService;
        this.authSessionSynchronizer = authSessionSynchronizer;
    }

    @GetMapping
    public String findAll(Model model) {

        List<PublicAccount> publicAccounts = publicAccountService.findAll();
        model.addAttribute("accounts", publicAccounts);

        return "account/index";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable("id") Long id) {

        PublicAccount publicAccount = publicAccountService.findById(id);
        model.addAttribute("account", publicAccount);

        return "account/show";
    }

    @GetMapping("/edit")
    public String editForm() {
        return "account/form";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User user, @Validated PublicAccountDto publicAccountDto,
                         RedirectAttributes redirectAttributes) {

        Long publicAccountId = user.getPublicAccount().getId();
        publicAccountService.update(publicAccountDto, publicAccountId);

        authSessionSynchronizer.refreshPrincipalInAuthSession(user.getId());

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Анаунт було успішно оновлено");
        return "redirect:/accounts/" + publicAccountId;
    }

}
