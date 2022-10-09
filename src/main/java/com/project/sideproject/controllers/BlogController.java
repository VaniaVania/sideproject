package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repository.PostRepository;
import com.project.sideproject.service.PostService;
import com.project.sideproject.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final StorageService storageService;

    @Autowired
    public BlogController(PostRepository postRepository, PostService postService, StorageService storageService) {
        this.postRepository = postRepository;
        this.postService = postService;
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
        if (postService.postShow(id,model, postRepository)) return "redirect:/blog";
        return "blog-details";
    }

    @RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> showImage(@PathVariable String filename){
        File file = storageService.load(filename).toFile();
        try(FileInputStream fis = new FileInputStream(file)){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(fis.readAllBytes(), headers, HttpStatus.CREATED);
        }
        catch (IOException ex){
            ex.getMessage();
        }
        return null;
    }

}
