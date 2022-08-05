package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repo.PostRepository;
import com.project.sideproject.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;


@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    private final PostRepository postRepository;
    private final StorageService storageService;

    @Autowired
    public BlogController(PostRepository postRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.storageService = storageService;
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

    @GetMapping("/add")
    public String blogAdd(Model model) {
        model.addAttribute("imageList", FileUploadController.getImageList());
        return "blog-add";
    }

    @PostMapping("/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text){
        Post post = new Post(title,anons,full_text,FileUploadController.getImageList(),new Date());
        postRepository.save(post);
        FileUploadController.getImageList().clear();
        return "redirect:/blog";
    }

    @GetMapping("/{id}/edit")
    public String editPost(@PathVariable(value = "id") Long id,Model model){
        if (blogFormer(id, model)) return "redirect:/blog";
        return "blog-edit";
    }

    @PatchMapping("/{id}/edit")
    public String editBlogPost(@PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);

        return "redirect:/blog";
    }

    @DeleteMapping("/{id}/delete")
    public String deletePost(@PathVariable(value = "id") Long id) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();

        for(String image: post.getImages_link()) {
            if(!post.getImages_link().isEmpty()){
                storageService.deleteFile("src/main/resources/static/images/" + image);
            }
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
