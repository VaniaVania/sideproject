package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repo.PostRepository;
import com.project.sideproject.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class BlogController {

    private final StorageService storageService;
    private final PostRepository postRepository;

    @Autowired
    public BlogController(PostRepository postRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.storageService = storageService;
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
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text , Model model){
        Date date = new Date();
        Post post = new Post(title,anons,full_text,FileUploadController.getImageLink(),date);
        postRepository.save(post);
        FileUploadController.getImageLink().clear();
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") Long id, Model model) {
        if (blogFormer(id, model)) return "redirect:/blog";
        return "blog-details";
    }


    @GetMapping("/blog/{id}/edit")
    public String editPost(@PathVariable(value = "id") Long id,Model model){
        if (blogFormer(id, model)) return "redirect:/blog";
        return "blog-edit";
    }

    @PatchMapping("/blog/{id}/edit")
    public String editBlogPost(@PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text,Model model){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);

        return "redirect:/blog";
    }

    @DeleteMapping("/blog/{id}/delete")
    public String deletePost(@PathVariable(value = "id") Long id, Model model) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();

        FileUploadController.getImageLink().clear();
        for(String image: post.getImages_link()) {
            System.out.println(image);
            storageService.deleteFile("src/main/resources/static" + image);
        }
        postRepository.delete(post);
        return "redirect:/blog";
    }

    private boolean blogFormer(@PathVariable("id") Long id, Model model) {
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
