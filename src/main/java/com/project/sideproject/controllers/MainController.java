package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.service.PostService;
import com.project.sideproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public MainController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {

        Iterable<Post> posts = postService.findAll();
        model.addAttribute("title", "Main page");
        model.addAttribute("posts", posts);

        return "home";
    }

    @GetMapping("/find")
    public String about(Model model) {
        model.addAttribute("title", "About us");
        return "car-finder";
    }

    @ModelAttribute
    public void isAuthorized(Model model) {
        model.addAttribute("username", userService.getUsername());
        model.addAttribute("isAuthenticated", userService.isAuthenticated());
        model.addAttribute("isAuthorized", userService.isAuthorized());
    }

}
