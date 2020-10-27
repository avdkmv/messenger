package com.unn.service;

import java.util.HashMap;
import java.util.Map;

import com.unn.model.User;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private Map<String, User> allUsers = new HashMap<>();

    public void addUser(User user) {
        allUsers.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return allUsers.get(username);
    }
}
