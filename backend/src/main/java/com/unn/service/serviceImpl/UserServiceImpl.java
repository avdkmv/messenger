package com.unn.service.serviceImpl;

import java.util.Optional;

import com.unn.model.User;
import com.unn.repository.UserRepo;
import com.unn.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepo userRepo;

    @Override
    public ResponseEntity<User> getUser(Long id) {
        logger.debug("get user");
        Optional<User> user = userRepo.getById(id);
        if(user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<User> addUser(User user) {
        logger.debug("set user");
        return ResponseEntity.ok(userRepo.save(user));
    }

}
