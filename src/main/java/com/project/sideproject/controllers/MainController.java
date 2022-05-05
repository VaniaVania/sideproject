package com.project.sideproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Controller
public class MainController {

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("title", "Main page");
        return "home";
    }


    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About us");
        return "about";
    }
}
