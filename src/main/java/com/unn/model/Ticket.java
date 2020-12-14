package com.unn.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ticket {
    private String creator;
    private String name;
    private String description;
    private String asignee;
    private Chat chat;

    private Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        chat.addUser(user);
    }

    public void removeUser(String username) {
        users.remove(username);
    }
}
