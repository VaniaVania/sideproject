package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.service.PostService;
import com.project.sideproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public AdminController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String adminPanel() {
        return "admin-panel";
    }

    @GetMapping("/edit")
    public String editPost(Model model) {
        Iterable<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);

        return "admin-blog-edit";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        model.addAttribute("imageList", postService.getImagesList());

        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String fullText) {
        Post post = new Post(title, anons, fullText, postService.getImagesList(), new Date());
        postService.save(post);
        postService.getImagesList().clear();
        return "redirect:/blog";
    }

    @GetMapping("blog/{id}/edit")
    public String editPost(@PathVariable(value = "id") Long id, Model model) {
        if (postService.postShow(id)) return "redirect:/blog";
        model.addAttribute("post", postService.getPost(id));

        return "blog-edit";
    }

    @PatchMapping("blog/{id}/edit")
    public String editPost(@PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String anons, @RequestParam String fullText) {
        Post post = postService.findById(id).orElseThrow();
        postService.edit(post,title,anons,fullText);

        return "redirect:/admin/edit";
    }

    @DeleteMapping("blog/{id}/delete")
    public String deletePost(@PathVariable(value = "id") Long id) throws IOException {
        Post post = postService.findById(id).orElseThrow();
        postService.delete(post);

        return "redirect:/admin/edit";
    }

    @ModelAttribute
    public void isAuthorized(Model model) {
        model.addAttribute("username", userService.getUsername());
        model.addAttribute("isAuthenticated", userService.isAuthenticated());
        model.addAttribute("isAuthorized", userService.isAuthorized());
    }

}
