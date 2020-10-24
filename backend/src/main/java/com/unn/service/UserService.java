package com.unn.service;

import com.unn.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<User> getUser(Long id);
    ResponseEntity<User> addUser(User user);
}
