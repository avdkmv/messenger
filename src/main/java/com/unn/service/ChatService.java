package com.unn.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.unn.dto.ChatRequest;
import com.unn.model.Chat;
import com.unn.model.User;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
    private Map<String, Chat> chats = new HashMap<>();

    private final UserService userService;

    public Chat addChat(final ChatRequest request) {
        Optional<User> creatorUser = userService.getUser(
            request.getUsername()
        );

        Chat newChat = new Chat();
        newChat.setId(request.getId());
        newChat.generateLink();
        newChat.setPrivate(false);

        if (creatorUser.isPresent()) {
            newChat.setCreator(creatorUser.get().getUsername());
            newChat.addUser(creatorUser.get());
        }

        Optional<Chat> chat = findChat(newChat);
        if (!chat.isPresent()) {
            this.chats.put(newChat.getId(), newChat);
            return newChat;
        }

        return chat.get();
    }

    public Chat addChat(final ChatRequest request, String creator, String other) {
        Optional<User> user = userService.getUser(other);
        Optional<User> creatorUser = userService.getUser(creator);

        Chat newChat = new Chat();
        newChat.setId(request.getId());
        newChat.generateLink();
        newChat.setPrivate(true);

        if (creatorUser.isPresent()) {
            newChat.setCreator(creator);
            newChat.addUser(creatorUser.get());
        }

        if (user.isPresent()) {
            newChat.addUser(user.get());
        }

        Optional<Chat> chat = findChat(newChat);
        if (!chat.isPresent()) {
            this.chats.put(newChat.getId(), newChat);
            return newChat;
        }

        return chat.get();
    }

    public Optional<Chat> findByIdSecured(String id, Authentication auth) {
        Optional<Chat> chat = findById(id);
        if (chat.isPresent() && chat.get().getUsers().containsKey(auth.getName())) {
            return chat;
        }

        return Optional.empty();
    }

    public Optional<Chat> findById(String id) {
        return Optional.ofNullable(chats.get(id));
    }

    public Optional<Chat> findChat(String name) {
        return Optional.ofNullable(this.chats.get(name));
    }

    public List<Chat> allChats() {
        return new ArrayList<>(this.chats.values());
    }

    public List<Chat> allChatsForUser(String userName) {
        return chats
            .values()
            .parallelStream()
            .filter(chat -> chat.getUsers().containsKey(userName))
            .collect(Collectors.toList());
    }

    public Collection<User> addUser(String chatName, String userName) {
        Optional<Chat> chat = findChat(chatName);

        if (chat.isPresent()) {
            if (userName != null) {
                Optional<User> optionalUser = userService.getUser(userName);

                optionalUser.ifPresent(user -> chat.get().addUser(user));
            }

            return chat.get().getUsers().values();
        }

        return List.of();
    }

    private boolean exists(Chat chat) {
        return (chats.containsKey(chat.getId()) || findChat(chat).isPresent());
    }

    private Optional<Chat> findChat(Chat chat) {
        return chats
            .values()
            .parallelStream()
            .filter(c -> c.getAllUsers().equals(chat.getAllUsers()))
            .findFirst();
    }
}
