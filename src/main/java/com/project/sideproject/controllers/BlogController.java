package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.service.ImageService;
import com.project.sideproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    private final PostService postService;
    private final ImageService imageService;

    @Autowired
    public BlogController(PostService postService, ImageService imageService) {
        this.postService = postService;
        this.imageService = imageService;
    }

    @GetMapping
    public String blogMain(Model model) {
        Iterable<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/{id}")
    public String blogDetails(@PathVariable(value = "id") Long id, Model model) {
        if (postService.notExistsById(id)) return "redirect:/blog";
        model.addAttribute("post", postService.getPost(id));
        return "blog-details";
    }

    @RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> showImage(@PathVariable String filename){
        return imageService.showImage(filename);
    }
}
