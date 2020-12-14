package com.unn.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.unn.dto.Message;

import org.springframework.security.core.Authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Chat {
    private String id;
    private String name = "Conversation";
    private String creator;
    private String link;

    private boolean isPrivate;

    private Map<String, User> users = new HashMap<>();
    private List<Message> messages = new ArrayList<>();

    public String generateLink() {
        link = "/chat/" + id;
        return link;
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public void newMessage(String message, Authentication auth) {
        messages.add(new Message(message, auth.getName(), Instant.now().toString(), false));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(this.users.values());
    }

    public List<User> getPrivateChatUsers() {
        if (isPrivate) {
            return getAllUsers();
        }

        return List.of();
    }

    public List<Message> messageHistory(int limit, String user) {
        if (limit > messages.size()) {
            limit = messages.size();
        }

        List<Message> msgHistory = messages.subList(messages.size() - limit, messages.size());
        msgHistory.forEach(msg -> msg.updateCurrentSenderFlag(user));
        return msgHistory;
    }

    public List<Message> messageHistory(String user) {
        return messageHistory(messages.size(), user);
    }

    public String generatePrivateName(String currentUser) {
        StringBuilder name = new StringBuilder("Chat with ");
        Optional<String> username = getPrivateChatUsers()
            .stream()
            .map(user -> user.getUsername())
            .filter(user -> !user.equals(currentUser))
            .findFirst();

        name.append(username.get());
        this.name = name.toString();
        return name.toString();
    }

    public String generatePublicName() {
        return this.name;
    }
}
