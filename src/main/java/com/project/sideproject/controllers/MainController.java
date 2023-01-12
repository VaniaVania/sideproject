package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    private final PostService postService;

    @Autowired
    public MainController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model){

        Iterable<Post> posts = postService.findAll();
        model.addAttribute("title", "Main page");
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/find")
    public String about(Model model){
        model.addAttribute("title", "About us");
        return "car-finder";
    }

}
