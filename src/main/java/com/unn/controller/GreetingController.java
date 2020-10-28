package com.unn.controller;

import com.unn.model.Greeting;
import com.unn.model.User;
import com.unn.service.UserService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GreetingController {
    private final UserService service;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(@RequestBody User user) {
        service.addUser(user);
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(user.getUsername()) + "!");
    }
}
