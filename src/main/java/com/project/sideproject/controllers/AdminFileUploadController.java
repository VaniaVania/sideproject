package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.repository.PostRepository;
import com.project.sideproject.service.PostService;
import com.project.sideproject.storage.StorageFileNotFoundException;
import com.project.sideproject.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

@Controller
@RequestMapping(value = "/admin/blog/")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminFileUploadController {

    private final StorageService storageService;
    private final PostService postService;


    @Autowired
    public AdminFileUploadController(StorageService storageService,PostService postService) {
        this.storageService = storageService;
        this.postService = postService;
    }


    @RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> ListImage(@PathVariable String filename) throws IOException{
        File file = storageService.load(filename).toFile();
        FileInputStream fis = new FileInputStream(file);
        byte [] image = fis.readAllBytes();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        fis.close();
        return new ResponseEntity<byte[]>(image, headers, HttpStatus.CREATED);
    }

    @PostMapping("/add/upload")
    public String addPrePostImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        storageService.store(file);
        postService.addPreImagesList(file.getOriginalFilename());
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/admin/blog/add";
    }

    @DeleteMapping("add/{filename}/delete")
    public String deletePrePostImage(@PathVariable String filename) throws IOException {
        storageService.deleteFile("src/main/resources/static/images/" + filename);
        postService.removePreImagesList(filename);

        return "redirect:/admin/blog/add";
    }

    @PostMapping("/{id}/edit/upload")
    public String addPostImage(@PathVariable(value = "id") Long id, @RequestParam("uploadFile") MultipartFile file) {
        storageService.store(file);
        postService.addImagesList(id,file.getOriginalFilename());

        return "redirect:/admin/blog/{id}/edit";
    }

    @DeleteMapping("/{id}/{filename}/delete")
    public String deletePostImage(@PathVariable(value = "id") Long id, @PathVariable String filename) throws IOException {
        storageService.deleteFile("src/main/resources/static/images/" + filename);
        postService.removeImagesList(id,filename);

        return "redirect:/admin/blog/{id}/edit";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}