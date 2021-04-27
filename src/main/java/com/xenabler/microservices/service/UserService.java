package com.xenabler.microservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenabler.microservices.model.User;
import com.xenabler.microservices.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private static final String TOPIC = "user-created";

    private final UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) throws JsonProcessingException {
        user.setId(UUID.randomUUID().toString());
        user = userRepository.save(user);   // Save in database
        kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(user));   // Publish the event as entire User object to topic
        log.info("User created");
        return user;
    }
}
