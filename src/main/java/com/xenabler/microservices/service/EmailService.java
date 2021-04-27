package com.xenabler.microservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenabler.microservices.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "user-created", groupId = "user-event-email-consumer-group", containerFactory = "emailKafkaListenerContainerFactory")
    public void consumeUserCreated(String message) throws JsonProcessingException {
        User user = objectMapper.readValue(message, User.class);
        log.info("Email sent to user {}", user.getFirstName());
    }
}
