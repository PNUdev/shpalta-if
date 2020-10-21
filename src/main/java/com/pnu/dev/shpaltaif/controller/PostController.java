package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.dto.filter.PostsPublicFilter;
import com.pnu.dev.shpaltaif.exception.ServiceException;
import com.pnu.dev.shpaltaif.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/partial")
    public String findAll(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
                                  Pageable pageable,
                          PostsPublicFilter postsPublicFilter,
                          Model model) {

        try {
            Page<Post> posts = postService.findAll(postsPublicFilter, pageable);
            model.addAttribute("posts", posts);
        } catch (ServiceException ex) {
            model.addAttribute("error", "Не знайдено");
        }

        return "post/indexPartial";
    }

    @GetMapping("/search-result-partial")
    public String getSearchResult(@RequestParam("title") String title,
                                  @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                  Model model) {
        model.addAttribute("posts", postService.findByTitleContains(title, size));
        return "post/searchResultPartial";
    }
}
