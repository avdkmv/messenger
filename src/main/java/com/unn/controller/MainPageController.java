package com.unn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    @GetMapping("/main")
    public String getMainPage(Model model) {
        // model.addAttribute("users", userService.getAllUsers());
        return "main";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }
}
