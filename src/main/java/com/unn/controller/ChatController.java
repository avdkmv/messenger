package com.unn.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.unn.dto.ChatRequest;
import com.unn.dto.ChatResponse;
import com.unn.dto.Message;
import com.unn.model.Chat;
import com.unn.service.ChatService;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat")
    @SendTo("/topic/chats")
    public boolean createChat(ChatRequest request, Authentication auth) {
        String currentUser = auth.getName();
        request.setId(UUID.randomUUID().toString());
        chatService.addChat(request, currentUser, request.getUsername());

        return true;
    }

    @MessageMapping("/chats")
    @SendToUser(destinations = "/queue/chats", broadcast = false)
    public List<ChatResponse> getAllChats(Authentication auth) {
        List<Chat> allChatsForUser = chatService.allChatsForUser(auth.getName());

        return allChatsForUser
            .stream()
            .filter(chat -> chat.isPrivate())
            .map(
                chat -> {
                    ChatResponse chatResponse = new ChatResponse(chat);
                    chatResponse.generatePrivateName(auth.getName());
                    return chatResponse;
                }
            )
            .collect(Collectors.toList());
    }

    @MessageMapping("/chat/{id}/message")
    @SendTo("/topic/chat/{id}")
    public boolean message(@DestinationVariable("id") String chatId, String text, Authentication auth) {
        Optional<Chat> chat = chatService.findByIdSecured(chatId, auth);
        if (chat.isPresent()) {
            chat.get().newMessage(text, auth);
        }

        return true;
    }

    @MessageMapping("/chat/{id}/messages")
    @SendToUser(destinations = "/queue/chat/{id}", broadcast = false)
    public List<Message> messages(@DestinationVariable("id") String chatName, Authentication auth) {
        log.info("Sent msg history to user {}", auth.getName());
        Optional<Chat> chat = chatService.findByIdSecured(chatName, auth);

        return chat.isPresent() ? chat.get().messageHistory(auth.getName()) : new ArrayList<>();
    }
    // @MessageMapping("/chat/{name}/user/{username}")
    // @SendTo("/topic/chat/{name}/users")
    // public Collection<User> addUser(
    //     @DestinationVariable("name") String chatName,
    //     @DestinationVariable("username") String userName
    // ) {
    //     Collection<User> userList = chatService.addUser(chatName, userName);
    //     userService.getUser(userName).ifPresent(user -> getAllChats(user.getUsername()));

    //     return userList;
    // }

    // @MessageMapping("/chat/{name}/user/{username}/delete")
    // @SendTo("/topic/chat/{name}/users")
    // public Collection<User> deleteUser(
    //     @DestinationVariable("name") String chatName,
    //     @DestinationVariable("username") String userName
    // ) {
    //     Optional<Chat> chat = chatService.findChat(chatName);

    //     if (chat.isPresent()) {
    //         if (userName != null) {
    //             chat.get().removeUser(userName);
    //         }

    //         return chat.get().getUsers().values();
    //     } else {
    //         return null;
    //     }
    // }

    // @MessageMapping("/chat/{name}/users")
    // @SendTo("/topic/chat/{name}/users")
    // public Collection<User> updateUsers(
    //     @DestinationVariable("name") String name,
    //     @CookieValue("username") String username
    // ) {
    //     Optional<Chat> chat = chatService.findChat(name);

    //     if (chat.isPresent()) {
    //         if (username != null) {
    //             userService.getUser(username).ifPresent(user -> chat.get().addUser(user));
    //         }

    //         return chat.get().getUsers().values();
    //     } else {
    //         return null;
    //     }
    // }

    // @MessageMapping("/chat/{name}/users/search")
    // @SendTo("/topic/chat/{name}/users/search")
    // public List<String> searchUsers(
    //     @DestinationVariable("name") String name,
    //     @CookieValue("username") String username,
    //     @RequestBody String searchQuery
    // ) {
    //     return userService.findUsers(searchQuery);
    // }
}
