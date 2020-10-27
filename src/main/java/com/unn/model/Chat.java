package com.unn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Chat {
    private String name;
    private String creator;
    private String link;

    private Map<String, User> users = new HashMap<>();
    private List<String> messages = new ArrayList<>();

    public String generateLink() {
        link = "/chat/" + name;
        return link;
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public void newMessage(String message) {
        messages.add(message);
    }

    public List<String> messageHistory(int limit) {
        if (limit > messages.size()) {
            limit = messages.size();
        }
        return messages.subList(messages.size() - limit, messages.size());
    }

    public List<String> messageHistory() {
        return messageHistory(messages.size());
    }
}
