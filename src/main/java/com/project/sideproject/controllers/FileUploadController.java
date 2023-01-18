package com.project.sideproject.controllers;

import com.project.sideproject.service.CreatePostService;
import com.project.sideproject.service.PostService;
import com.project.sideproject.storage.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping(value = "/admin/blog/")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class FileUploadController {

    private final PostService postService;
    private final CreatePostService createPostService;

    @Autowired
    public FileUploadController(PostService postService, CreatePostService createPostService) {
        this.postService = postService;
        this.createPostService = createPostService;
    }

    @PostMapping("/add/upload")
    public String addPrePostImage(@RequestParam("file") MultipartFile file) {
        createPostService.addPrePostImage(file);
        return "redirect:/admin/blog/add";
    }

    @DeleteMapping("add/{filename}/delete")
    public String removePrePostImage(@PathVariable String filename) throws IOException {
        createPostService.removePrePostImage(filename);
        return "redirect:/admin/blog/add";
    }

    @PostMapping("/{id}/edit/upload")
    public String addPostImage(@PathVariable(value = "id") Long id, @RequestParam("uploadFile") MultipartFile file) {
        postService.addPostImage(id, file);
        return "redirect:/admin/blog/{id}/edit";
    }

    @DeleteMapping("/{id}/{filename}/delete")
    public String removePostImage(@PathVariable(value = "id") Long id, @PathVariable String filename) throws IOException {
        postService.removePostImage(id, filename);
        return "redirect:/admin/blog/{id}/edit";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ModelAttribute
    public void attributes(RedirectAttributes attributes) {
        attributes.addFlashAttribute("message",
                "You successfully uploaded file!");

        attributes.addFlashAttribute("wrong",
                "File already exists!");
    }

}