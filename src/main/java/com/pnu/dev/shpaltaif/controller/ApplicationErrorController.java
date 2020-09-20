package com.pnu.dev.shpaltaif.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationErrorController implements ErrorController {

    @RequestMapping("/error")
    public String showErrorPage(Model model) {
        return "error/show";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
