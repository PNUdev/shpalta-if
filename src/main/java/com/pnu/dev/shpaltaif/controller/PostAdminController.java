package com.pnu.dev.shpaltaif.controller;

import com.pnu.dev.shpaltaif.domain.Post;
import com.pnu.dev.shpaltaif.domain.PublicAccount;
import com.pnu.dev.shpaltaif.domain.User;
import com.pnu.dev.shpaltaif.dto.PostDto;
import com.pnu.dev.shpaltaif.dto.PostFiltersDto;
import com.pnu.dev.shpaltaif.service.CategoryService;
import com.pnu.dev.shpaltaif.service.PostService;
import com.pnu.dev.shpaltaif.service.PublicAccountService;
import com.pnu.dev.shpaltaif.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.pnu.dev.shpaltaif.util.FlashMessageConstants.FLASH_MESSAGE_SUCCESS;

@Controller
@RequestMapping("/admin/posts")
public class PostAdminController {

    private final PostService postService;

    private final CategoryService categoryService;

    private final PublicAccountService publicAccountService;

    @Autowired
    public PostAdminController(PostService postService, CategoryService categoryService, PublicAccountService publicAccountService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.publicAccountService = publicAccountService;
    }


    @GetMapping
    public String findAll(@AuthenticationPrincipal User user, Model model,
                          @PageableDefault(size = 10,
                                  sort = "createdAt",
                                  direction = Sort.Direction.DESC)
                                  Pageable pageable,
                          PostFiltersDto postFiltersDto) {

        Page<Post> posts = postService.findAll(user, postFiltersDto, pageable);
        List categories = categoryService.findAll();
        List<PublicAccount> publicAccounts = publicAccountService.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("publicAccounts", publicAccounts);
        model.addAttribute("posts", posts);
        model.addAttribute("postFilters", postFiltersDto);
        return "admin/post/index";
    }

    @GetMapping("/{id}")
    public String findById(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {

        Post post = postService.findById(user, id);

        model.addAttribute("post", post);
        return "admin/post/show";
    }

    @GetMapping("/new")
    public String createForm(Model model) {

        List categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/post/form";
    }

    @PostMapping("/new")
    public String create(@AuthenticationPrincipal User user, @Validated PostDto postDto) {

        postService.create(user, postDto);
        return "redirect:/admin/posts";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {

        Post post = postService.findById(user, id);
        List categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("post", post);

        return "admin/post/form";
    }

    @PostMapping("/update/{id}")
    public String update(@AuthenticationPrincipal User user, @PathVariable("id") Long id, @Validated PostDto postDto,
                         RedirectAttributes redirectAttributes) {

        System.out.println(postDto.toString());
        postService.update(user, id, postDto);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Пост успішно оновлено!");

        return "redirect:/admin/posts";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateConfirmation(@PathVariable("id") Long id, Model model,
                                         HttpServletRequest request) {

        model.addAttribute("message", "Ви впевнені, що справді хочете перемістити пост в архів?");
        model.addAttribute("returnBackUrl", HttpUtils.getPreviousPageUrl(request));
        model.addAttribute("actionUrl", "/admin/posts/deactivate/" + id);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivate(@AuthenticationPrincipal User user, @PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes) {

        postService.deactivate(user, id);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Пост переміщено в архів!");

        return "redirect:/admin/posts";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirmation(@PathVariable("id") Long id, Model model,
                                     HttpServletRequest request) {

        model.addAttribute("message", "Ви впевнені, що справді хочете видалити пост?");
        model.addAttribute("returnBackUrl", HttpUtils.getPreviousPageUrl(request));
        model.addAttribute("actionUrl", "/admin/posts/delete/" + id);

        return "admin/common/deleteConfirmation";
    }

    @PostMapping("/delete/{id}")
    public String delete(@AuthenticationPrincipal User user, @PathVariable("id") Long id,
                         RedirectAttributes redirectAttributes) {

        postService.delete(user, id);

        redirectAttributes.addFlashAttribute(FLASH_MESSAGE_SUCCESS, "Пост успішно видалено!");

        return "redirect:/admin/posts";
    }
}
