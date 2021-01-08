package com.pnu.dev.shpaltaif.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedController {

    @GetMapping(value = {"/", "/feed", "/feed/{category}"})
    public String showFeed(@PathVariable(value = "category", required = false) String category,
                           @RequestParam(value = "sort", required = false) String sort,
                           @RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "author", required = false) String authorPublicAccountId,
                           Model model) {

        model.addAttribute("category", category);
        model.addAttribute("sort", sort);
        model.addAttribute("title", title);
        model.addAttribute("authorPublicAccountId", authorPublicAccountId);

        return "feed/index";
    }

}
