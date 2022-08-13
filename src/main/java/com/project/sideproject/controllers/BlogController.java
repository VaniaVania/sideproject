package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.project.sideproject.controllers.AdminController.postShow;


@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    private final PostRepository postRepository;

    @Autowired
    public BlogController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/{id}")
    public String blogDetails(@PathVariable(value = "id") Long id, Model model) {
        if (blogFormer(id, model)) return "redirect:/blog";
        return "blog-details";
    }

    private boolean blogFormer(@PathVariable("id") Long id, Model model) {
        return postShow(id, model, postRepository);
    }

}
