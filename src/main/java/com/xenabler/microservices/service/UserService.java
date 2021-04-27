package com.xenabler.microservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenabler.microservices.model.Payment;
import com.xenabler.microservices.model.User;
import com.xenabler.microservices.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private static final String TOPIC_USER_CREATED = "user-created";
    private static final String TOPIC_USER_ACTIVATED = "user-activated";

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
        user.setActivated(false);
        user = userRepository.save(user);   // Save in database

        kafkaTemplate.send(TOPIC_USER_CREATED, objectMapper.writeValueAsString(user));   // Publish the event as entire User object to topic
        log.info("User created");
        return user;
    }

    @KafkaListener(topics = "payment-activated",
            groupId = "user-service-consumer-group",
            containerFactory = "userKafkaListenerContainerFactory")
    public void consumeUserCreated(String message) throws JsonProcessingException {
        Payment payment = objectMapper.readValue(message, Payment.class);

        // Activate the user
        User user = userRepository.findById(payment.getUserId()).get();
        user.setActivated(true);
        userRepository.save(user);

        // Publish user activated event
        kafkaTemplate.send(TOPIC_USER_ACTIVATED, objectMapper.writeValueAsString(user));
        log.info("User activated");
    }

    public User getUser(String id) {
        return userRepository.findById(id).get();
    }
}
