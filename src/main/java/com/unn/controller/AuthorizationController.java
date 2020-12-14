package com.unn.controller;

import com.unn.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {
    private final UserService userService;

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/login")
    public String getLoginForm(
        @RequestParam(name = "error", required = false) boolean error,
        @RequestParam(name = "signout", required = false) boolean signout,
        Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("signout", signout);
        return "login";
    }

    @GetMapping("/signup")
    public String getSignupForm(@RequestParam(name = "error", required = false) boolean error, Model model) {
        model.addAttribute("error", error);
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username, @RequestParam("password") String password) {
        try {
            userService.signup(username, password);
        } catch (IllegalArgumentException ex) {
            log.error("Signup failed");
            return "redirect:/signup?error=true";
        }

        template.convertAndSend("/topic/users", true);
        return "redirect:/main";
    }
}
