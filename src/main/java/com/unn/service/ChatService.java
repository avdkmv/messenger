package com.unn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.unn.model.Chat;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ChatService {
    private Map<String, Chat> chats = new HashMap<>();

    private static final List<String> curseWords = Arrays.asList("сука", "блять", "хуй", "пидор");
    public static final String CURSED_MESSAGE = "не матерись, блять!";

    public void addChat(final Chat chat) {
        if (!this.chats.containsKey(chat.getName())) {
            this.chats.put(chat.getName(), chat);
        }
    }

    public Optional<Chat> findChat(String name) {
        return Optional.ofNullable(this.chats.get(name));
    }

    public List<Chat> allChats() {
        return new ArrayList<>(this.chats.values());
    }

    public boolean isCursed(String text) {
        return curseWords.parallelStream().anyMatch(word -> text.toLowerCase().contains(word));
    }
}
