package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repo.PostRepository;
import com.project.sideproject.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {


    private final PostRepository postRepository;
    private final StorageService storageService;

    @Autowired
    public BlogController(StorageService storageService, PostRepository postRepository) {
        this.storageService = storageService;
        this.postRepository = postRepository;
    }


    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, @RequestParam List<String> fileLink, Model model){
        fileLink = FileUploadController.getFileLink();
        Post post = new Post(title,anons,full_text,fileLink);

        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        if (blogFormer(id, model)) return "redirect:/blog";
        return "blog-details";
    }


    @GetMapping("/blog/{id}/edit")
    public String editPost(@PathVariable(value = "id") long id,Model model){
        if (blogFormer(id, model)) return "redirect:/blog";
        return "blog-edit";
    }

    @PatchMapping("/blog/{id}/edit")
    public String editBlogPost(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text,Model model){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);

        return "redirect:/blog";
    }

    @DeleteMapping("/blog/{id}/delete")
    public String deletePost(@PathVariable(value = "id") long id, Model model) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        storageService.deleteFile("src/main/resources/static" + post.getFileLink());

        return "redirect:/blog";
    }

    private boolean blogFormer(@PathVariable("id") long id, Model model) {
        if(!postRepository.existsById(id)){
            return true;
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return false;
    }



}
