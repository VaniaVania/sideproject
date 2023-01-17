package com.project.sideproject.controllers;

import com.project.sideproject.models.Role;
import com.project.sideproject.models.User;
import com.project.sideproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "user-list";
    }

    @GetMapping("/{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "user-edit";
    }

    @PatchMapping
    public String userSave(@RequestParam("userId") User user, @RequestParam Map<String, String> form, @RequestParam String userName) {
        user.setUsername(userName);
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userService.save(user);

        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") Long id, Model model) {
        User userFromDb = userService.findById(id).orElseThrow();
        userService.delete(userFromDb);
        model.addAttribute("user", userFromDb);

        return "redirect:/admin/users";
    }



}
