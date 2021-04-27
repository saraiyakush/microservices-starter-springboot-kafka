package com.xenabler.microservices.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xenabler.microservices.model.User;
import com.xenabler.microservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) throws JsonProcessingException {
        return userService.createUser(user);
    }
}
