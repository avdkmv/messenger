package com.unn.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import com.unn.dto.Message;
import org.springframework.security.core.Authentication;
import lombok.Data;

@Data
public class Chat {
    private String id;
    private String name = "Conversation";
    private String creator;
    private String link;

    private boolean isPrivate;

    private Map<String, User> users = new HashMap<>();
    private List<Message> messages = new CopyOnWriteArrayList<>();

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
        messages.add(new Message(message, auth.getName(), Instant.now().toString()));
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

        final List<Message> msgHistory = new CopyOnWriteArrayList<>();
        for (int i = messages.size() - limit; i < messages.size(); i++) {
            msgHistory.add(new Message(messages.get(i), user));
        }

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
        return name.toString();
    }

    public String generatePublicName() {
        return this.name;
    }
}
