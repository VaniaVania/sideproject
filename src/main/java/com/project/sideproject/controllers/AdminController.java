package com.project.sideproject.controllers;

import com.project.sideproject.models.Post;
import com.project.sideproject.models.Role;
import com.project.sideproject.models.User;
import com.project.sideproject.repo.PostRepository;
import com.project.sideproject.repo.UserRepository;
import com.project.sideproject.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public AdminController(UserRepository userRepository, PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping
    public String adminPanel(Model model){
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

    @GetMapping("/edit")
    public String editPost(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "admin-blog-edit";
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




}
