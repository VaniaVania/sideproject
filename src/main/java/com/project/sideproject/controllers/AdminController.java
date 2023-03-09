package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.service.CreatePostService;
import com.project.sideproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final PostService postService;
    private final CreatePostService createPostService;

    @Autowired
    public AdminController(PostService postService, CreatePostService createPostService) {
        this.postService = postService;
        this.createPostService = createPostService;
    }

    @GetMapping
    public String adminPanel() {
        return "admin-panel";
    }

    @GetMapping("/edit")
    public String adminPostList(Model model) {
        Iterable<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);

        return "admin-blog-edit";
    }

    @GetMapping("/blog/add")
    public String addPost(Model model) {
        model.addAttribute("imageList", createPostService.getImagesList());

        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String addPost(@RequestParam String title, @RequestParam String anons, @RequestParam String fullText) {
        Post post = new Post(title, anons, fullText, createPostService.getImagesList(), new Date());
        postService.save(post);
        createPostService.getImagesList().clear();

        return "redirect:/blog";
    }

    @GetMapping("blog/{id}/edit")
    public String adminPostList(@PathVariable(value = "id") Long id, Model model) {
        if (postService.notExistsById(id)) return "redirect:/blog";
        model.addAttribute("post", postService.getPost(id));

        return "blog-edit";
    }

    @PatchMapping("blog/{id}/edit")
    public String adminPostList(@PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String anons, @RequestParam String fullText) {
        Post post = postService.findById(id).orElseThrow();
        postService.edit(post, title, anons, fullText);

        return "redirect:/admin/edit";
    }

    @DeleteMapping("blog/{id}/delete")
    public String deletePost(@PathVariable(value = "id") Long id) throws IOException {
        Post post = postService.findById(id).orElseThrow();
        postService.delete(post);

        return "redirect:/admin/edit";
    }
}
