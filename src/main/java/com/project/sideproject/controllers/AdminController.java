package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.models.Role;
import com.project.sideproject.models.User;
import com.project.sideproject.repository.PostRepository;
import com.project.sideproject.repository.UserRepository;
import com.project.sideproject.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StorageService storageService;

    @Autowired
    public AdminController(UserRepository userRepository, PostRepository postRepository, StorageService storageService){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.storageService = storageService;
    }

    @GetMapping
    public String adminPanel(){
        return "admin-panel";
    }


    @GetMapping("/users")
    public String userList(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user",user);
        model.addAttribute("roles", Role.values());
        return "user-edit";
    }

    @PatchMapping
    public String userSave(@RequestParam("userId") User user, @RequestParam Map<String, String> form, @RequestParam String userName){
        user.setUsername(userName);
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();

        for(String key : form.keySet()){
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") Long id, Model model){
        User userFromDb = userRepository.findById(id).orElseThrow();
        userRepository.delete(userFromDb);
        model.addAttribute("user", userFromDb);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit")
    public String editPost(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "admin-blog-edit";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        model.addAttribute("imageList", AdminFileUploadController.getImageList());
    model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(AdminFileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
            .collect(Collectors.toList()));
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String fullText){
        Post post = new Post(title,anons,fullText, AdminFileUploadController.getImageList(),new Date());
        postRepository.save(post);
        AdminFileUploadController.getImageList().clear();
        return "redirect:/blog";
    }


    @GetMapping("blog/{id}/edit")
    public String editPost(@PathVariable(value = "id") Long id,Model model){
        if (blogFormer(id, model)) return "redirect:/blog";
        return "blog-edit";
    }

    @PatchMapping("blog/{id}/edit")
    public String editBlogPost(@PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String anons, @RequestParam String fullText){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(fullText);
        postRepository.save(post);

        return "redirect:/blog";
    }

    @DeleteMapping("blog/{id}/delete")
    public String deletePost(@PathVariable(value = "id") Long id) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();

        if(!post.getImages().isEmpty()){
        for(String image: post.getImages()) {
                storageService.deleteFile("src/main/resources/static/images/" + image);
            }
        }

        postRepository.delete(post);
        return "redirect:/blog";
    }

    private boolean blogFormer(@PathVariable("id") Long id, Model model) {
        return postShow(id, model, postRepository);
    }

    static boolean postShow(@PathVariable("id") Long id, Model model, PostRepository postRepository) {
        if(!postRepository.existsById(id)){
            return true;
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return false;
    }
}
