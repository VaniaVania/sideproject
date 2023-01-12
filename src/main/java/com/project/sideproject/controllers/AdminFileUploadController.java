package com.project.sideproject.controllers;

import com.project.sideproject.service.FileSystemStorageService;
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
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminFileUploadController {

    private final StorageService storageService;
    private final PostService postService;
    private final FileSystemStorageService fileSystemStorageService;


    @Autowired
    public AdminFileUploadController(StorageService storageService, PostService postService, FileSystemStorageService fileSystemStorageService) {
        this.storageService = storageService;
        this.postService = postService;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @PostMapping("/add/upload")
    public String addPrePostImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){

        if(!fileSystemStorageService.load(file.getOriginalFilename()).toFile().exists()) {
            storageService.store(file);
            postService.addPrePostImage(file.getOriginalFilename());

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
        }

            redirectAttributes.addFlashAttribute("wrong",
                    "File " + file.getOriginalFilename()  + " already exists!");

        return "redirect:/admin/blog/add";
    }

    @DeleteMapping("add/{filename}/delete")
    public String removePrePostImage(@PathVariable String filename) throws IOException {
        storageService.deleteFile("src/main/resources/static/images/" + filename);
        postService.removePrePostImage(filename);

        return "redirect:/admin/blog/add";
    }

    @PostMapping("/{id}/edit/upload")
    public String addPostImage(@PathVariable(value = "id") Long id, @RequestParam("uploadFile") MultipartFile file, RedirectAttributes redirectAttributes) {

        if(!fileSystemStorageService.load(file.getOriginalFilename()).toFile().exists()) {
        storageService.store(file);
        postService.addPostImage(id,file.getOriginalFilename());

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
        }

        redirectAttributes.addFlashAttribute("wrong",
                "File " + file.getOriginalFilename()  + " already exists!");

        return "redirect:/admin/blog/{id}/edit";
    }

    @DeleteMapping("/{id}/{filename}/delete")
    public String removePostImage(@PathVariable(value = "id") Long id, @PathVariable String filename) throws IOException {
        storageService.deleteFile("src/main/resources/static/images/" + filename);
        postService.removePostImage(id,filename);

        return "redirect:/admin/blog/{id}/edit";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}