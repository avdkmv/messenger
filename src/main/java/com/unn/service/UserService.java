package com.unn.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.unn.constants.UserRoles;
import com.unn.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder encoder;

    private Map<String, User> allUsers = new HashMap<>();

    public void addUser(User user) {
        allUsers.put(user.getUsername(), user);
    }

    public Optional<User> getUser(String username) {
        return Optional.ofNullable(allUsers.get(username));
    }

    public List<String> findUsers(String searchQuery) {
        return allUsers
            .keySet()
            .parallelStream()
            .filter(name -> name.toLowerCase().contains(searchQuery.toLowerCase()))
            .collect(Collectors.toList());
    }

    public boolean exist(User user) {
        return exist(user.getUsername());
    }

    public boolean exist(String username) {
        return allUsers.keySet().contains(username);
    }

    public void signup(String username, String password) {
        if (!exist(username)) {
            User user = new User(
                username,
                encoder.encode(password),
                Arrays.asList(new SimpleGrantedAuthority(UserRoles.USER.toString())),
                true,
                true,
                true,
                true
            );

            addUser(user);
        }
    }

    public List<User> getAllUsers() {
        return allUsers.values().stream().collect(Collectors.toList());
    }

    public List<User> getAllUsers(Authentication auth) {
        return allUsers
            .values()
            .stream()
            .filter(user -> !user.getUsername().equals(auth.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username).orElseThrow(() -> new UsernameNotFoundException("User doest not exist"));
    }
}
