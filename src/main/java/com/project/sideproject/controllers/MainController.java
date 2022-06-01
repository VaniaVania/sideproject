package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;


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


    @GetMapping("/about")
    public String about(Model model) throws IOException {
        model.addAttribute("title", "About us");
        return "about";
    }

}
