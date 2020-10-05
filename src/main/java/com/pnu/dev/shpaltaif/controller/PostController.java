package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        Post post = postService.findActiveById(id);
        model.addAttribute("post", post);
        return "post/show";
    }
}
