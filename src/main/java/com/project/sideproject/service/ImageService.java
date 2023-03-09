package com.project.sideproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class ImageService {

    private final StorageService storageService;

    @Autowired
    public ImageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public ResponseEntity<byte[]> showImage(String filename) {
        File file = storageService.load(filename).toFile();
        try (FileInputStream fis = new FileInputStream(file)) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(fis.readAllBytes(), headers, HttpStatus.CREATED);
        } catch (
                IOException ex) {
            ex.getMessage();
        }
        return null;
    }
}
