package com.pnu.dev.shpaltaif.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeedController {

    @GetMapping
    public String showFeed() {
        return "feed/index";
    }

}
