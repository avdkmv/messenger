package com.unn.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.unn.model.Chat;
import com.unn.service.ChatService;
import com.unn.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatPageController {
    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/chat/{name}")
    public ResponseEntity<String> addUserToChat(
        @PathVariable("name") String name,
        @RequestBody(required = false) String username,
        final HttpServletRequest req
    ) {
        Optional<Chat> chat = chatService.findChat(name);

        if (chat.isPresent()) {
            if (username != null) {
                chat.get().addUser(userService.getUser(username));
            }

            return ResponseEntity.ok(req.getRequestURI());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/chat/{name}")
    public String getChatPage(@PathVariable("name") String name, Model model) {
        Optional<Chat> chat = chatService.findChat(name);

        if (chat.isPresent()) {
            model.addAttribute("name", chat.get().getName());
            model.addAttribute("creator", chat.get().getCreator());
            model.addAttribute("members", chat.get().getUsers().values());
            return "chat";
        } else {
            return null;
        }
    }
}
