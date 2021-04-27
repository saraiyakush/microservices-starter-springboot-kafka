package com.xenabler.microservices.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xenabler.microservices.model.User;
import com.xenabler.microservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody User user) throws JsonProcessingException {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> get(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }
}
