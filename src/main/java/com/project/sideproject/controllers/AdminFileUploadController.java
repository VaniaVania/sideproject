package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repository.PostRepository;
import com.project.sideproject.storage.StorageFileNotFoundException;
import com.project.sideproject.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/blog/")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminFileUploadController {

    private final StorageService storageService;
    private final PostRepository postRepository;
    private static final List<String> imagesList = new ArrayList<>();


    @Autowired
    public AdminFileUploadController(StorageService storageService, PostRepository postRepository) {
        this.storageService = storageService;
        this.postRepository = postRepository;
    }

    @PostMapping("/add/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        storageService.store(file);
        imagesList.add(file.getOriginalFilename());

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/admin/blog/add";
    }

    @PostMapping("/{id}/edit/upload")
    public String updateImageList(@PathVariable(value = "id") Long id, @RequestParam("uploadFile") MultipartFile file){
        storageService.store(file);
        Post post = postRepository.findById(id).orElseThrow();
        post.getImages().add(file.getOriginalFilename());
        postRepository.save(post);
        return "redirect:/admin/blog/{id}/edit";
    }


    @DeleteMapping("/{id}/{filename}/delete")
    public String deletePostImage(@PathVariable Long id, @PathVariable String filename) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();
        storageService.deleteFile("src/main/resources/static/images/" + filename);
        post.getImages().remove(filename);
        postRepository.save(post);
        return "redirect:/admin/blog/{id}/edit";
    }

    @DeleteMapping("add/{filename}/delete")
    public String deletePrePostImage(@PathVariable String filename) throws IOException {
        storageService.deleteFile("src/main/resources/static/images/" + filename);
        imagesList.remove(filename);

        return "redirect:/admin/blog/add";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    public static List<String> getImageList() {
        return imagesList;
    }
}