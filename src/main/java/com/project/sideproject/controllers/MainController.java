package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @Autowired
    private PostRepository postRepository;


    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Main page");
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/find")
    public String about(Model model){
        model.addAttribute("title", "About us");
        return "car-finder";
    }

}
