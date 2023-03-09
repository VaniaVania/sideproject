package com.project.sideproject.util;

import com.project.sideproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalAttributeHandler {

    private final UserService userService;

    @Autowired
    public GlobalAttributeHandler(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void isAuthorized(Model model) {
        if (userService.getAuthentication() != null) {
            model.addAttribute("username", userService.getUsername());
            model.addAttribute("isAuthenticated", userService.isAuthenticated());
            model.addAttribute("isAuthorized", userService.isAuthorized());
        }
    }
}
