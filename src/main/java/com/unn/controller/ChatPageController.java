package com.unn.controller;

import java.util.Optional;

import com.unn.model.Chat;
import com.unn.service.ChatService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatPageController {
    private final ChatService chatService;

    @GetMapping("/chat/{id}")
    public String openChat(@PathVariable("id") String id, Authentication auth, Model model) {
        Optional<Chat> chat = chatService.findByIdSecured(id, auth);
        if (chat.isPresent()) {
            model.addAttribute("creator", chat.get().getCreator());
            model.addAttribute("id", chat.get().getId());
            model.addAttribute("name", chat.get().generatePrivateName(auth.getName()));
            return "chat";
        }

        return "redirect:/main";
    }
}
