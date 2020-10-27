package com.unn.controller;

import java.util.Optional;

import com.unn.model.Chat;
import com.unn.service.ChatService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatPageController {
    private final ChatService service;

    @GetMapping("/chat/{name}")
    public String getChatPage(@PathVariable("name") String name, @RequestBody String username, Model model) {
        Optional<Chat> chat = service.findChat(name);

        if (chat.isPresent()) {
            model.addAttribute("name", chat.get().getName());
            model.addAttribute("creator", chat.get().getCreator());
            model.addAttribute("members", chat.get().getUsers());
            return "chat";
        } else {
            return null;
        }
    }
}
