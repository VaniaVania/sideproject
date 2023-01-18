package com.project.sideproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreatePostService {

    private final StorageService storageService;
    private final List<String> imagesList = new ArrayList<>();

    @Autowired
    public CreatePostService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void addPrePostImage(MultipartFile file) {
        if (!storageService.load(file.getOriginalFilename()).toFile().exists()) {
            storageService.store(file);
            imagesList.add(file.getOriginalFilename());
        }
    }

    public void removePrePostImage(String filename) throws IOException {
        storageService.deleteFile("src/main/resources/static/images/" + filename);
        imagesList.remove(filename);
    }

    public List<String> getImagesList() {
        return imagesList;
    }
}
