package com.project.sideproject.service;


import com.project.sideproject.models.Post;
import com.project.sideproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final List<String> imagesList = new ArrayList<>();

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Post findByImage(String image){
        return postRepository.findByImages(image);
    }

    public boolean postShow(@PathVariable("id") Long id, Model model, PostRepository postRepository) {
        if(!postRepository.existsById(id)){
            return true;
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return false;
    }

    public void addPreImagesList(String filename){
        imagesList.add(filename);
    }

    public void removePreImagesList(@PathVariable String filename){
        imagesList.remove(filename);
    }

    public void addImagesList(@PathVariable(value = "id") Long id, String filename){
        Post post = postRepository.findById(id).orElseThrow();
        post.getImages().add(filename);
        postRepository.save(post);
    }

    public void removeImagesList(@PathVariable(value = "id") Long id, @PathVariable String filename){
        Post post = postRepository.findById(id).orElseThrow();
        post.getImages().remove(filename);
        postRepository.save(post);
    }

    public List<String> getImagesList() {
        return imagesList;
    }
}
