package com.unn.controller;

import java.util.List;

import com.unn.model.User;
import com.unn.service.UserService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @MessageMapping("/users/all")
    @SendToUser(destinations = "/queue/users", broadcast = false)
    public List<User> getAllUsers(Authentication auth) {
        return userService.getAllUsers(auth);
    }
}
