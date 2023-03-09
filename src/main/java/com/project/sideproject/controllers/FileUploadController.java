package com.project.sideproject.controllers;

import com.project.sideproject.service.CreatePostService;
import com.project.sideproject.service.PostService;
import com.project.sideproject.service.StorageService;
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
    private final StorageService storageService;

    @Autowired
    public FileUploadController(PostService postService, CreatePostService createPostService, StorageService storageService) {
        this.postService = postService;
        this.createPostService = createPostService;
        this.storageService = storageService;
    }

    @PostMapping("/add/upload")
    public String addPrePostImage(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (storageService.load(file.getOriginalFilename()).toFile().exists()) {
            attributes.addAttribute("wrong",
                    "File already exists!");
            return "redirect:/admin/blog/add";
        }
        createPostService.addPrePostImage(file);
        return "redirect:/admin/blog/add";
    }

    @DeleteMapping("add/{filename}/delete")
    public String removePrePostImage(@PathVariable String filename) throws IOException {
        createPostService.removePrePostImage(filename);
        return "redirect:/admin/blog/add";
    }

    @PostMapping("/{id}/edit/upload")
    public String addPostImage(@PathVariable(value = "id") Long id, @RequestParam("uploadFile") MultipartFile file, RedirectAttributes attributes) {
        if (storageService.load(file.getOriginalFilename()).toFile().exists()) {
            attributes.addFlashAttribute("wrong",
                    "File already exists!");
            return "redirect:/admin/blog/{id}/edit";
        }
        System.out.println(storageService.load(file.getOriginalFilename()).toFile().exists());
        System.out.println(storageService.load(file.getOriginalFilename()).toFile());
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


}