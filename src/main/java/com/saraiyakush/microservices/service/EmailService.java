package com.saraiyakush.microservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saraiyakush.microservices.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private static final String TOPIC_CREATE_USER_EMAIL_FAILURE = "create-user-email-failed";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = {"user-created", "user-activated"},
            groupId = "email-service-consumer-group",
            containerFactory = "emailKafkaListenerContainerFactory")
    public void consumeUserCreated(String message) throws JsonProcessingException {
        User user = objectMapper.readValue(message, User.class);

        // Use the failure field from User to determine if there was a failure or not in sending the email
        // This is just to demonstrate how to rollback already committed User and Payment objects
        if(user.getFailure()) {
            log.info("Email NOT sent to user {}", user.getFirstName());
            kafkaTemplate.send(TOPIC_CREATE_USER_EMAIL_FAILURE, objectMapper.writeValueAsString(user));
        }
        else {
            log.info("Email sent to user {}", user.getFirstName());
        }

    }
}
