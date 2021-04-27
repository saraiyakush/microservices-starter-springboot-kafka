package com.xenabler.microservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenabler.microservices.model.Payment;
import com.xenabler.microservices.model.PaymentRepository;
import com.xenabler.microservices.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PaymentService {
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(topics = "user-created", groupId = "user-event-payment-consumer-group", containerFactory = "paymentKafkaListenerContainerFactory")
    public void consumeUserCreated(String message) throws JsonProcessingException {
        User user = objectMapper.readValue(message, User.class);

        // Insert a default payment method
        Payment defaultPayment = new Payment();
        defaultPayment.setId(UUID.randomUUID().toString());
        defaultPayment.setUserId(user.getId());
        defaultPayment.setActivated(false);

        paymentRepository.save(defaultPayment);
        log.info("Default payment created for user");
    }
}
