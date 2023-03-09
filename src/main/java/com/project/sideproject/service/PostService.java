package com.project.sideproject.service;

import com.project.sideproject.models.Post;
import com.project.sideproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final StorageService storageService;

    @Autowired
    public PostService(PostRepository postRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.storageService = storageService;
    }

    public ArrayList<Post> getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        return res;
    }

    public void addPostImage(Long id, MultipartFile file) {
        Post post = postRepository.findById(id).orElseThrow();
        post.getImages().add(file.getOriginalFilename());
        storageService.store(file);
    }

    public void removePostImage(Long id, String filename) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();
        post.getImages().remove(filename);
        storageService.deleteFile("src/main/resources/static/images/" + filename);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void edit(Post post, String title, String anons, String fullText) {
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(fullText);
        postRepository.save(post);
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public void delete(Post post) throws IOException {

        if (!post.getImages().isEmpty()) {
            post.getImages().forEach(image -> {
                try {
                    storageService.deleteFile("src/main/resources/static/images/" + image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        postRepository.delete(post);
    }

    public boolean notExistsById(Long id) {
        return !postRepository.existsById(id);
    }


}
