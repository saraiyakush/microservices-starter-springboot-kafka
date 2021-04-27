package com.saraiyakush.microservices.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saraiyakush.microservices.model.Payment;
import com.saraiyakush.microservices.model.PaymentRepository;
import com.saraiyakush.microservices.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    private static final String TOPIC_PAYMENT_ACTIVATED = "payment-activated";

    private final PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(topics = "user-created",
            groupId = "payment-service-consumer-group",
            containerFactory = "paymentKafkaListenerContainerFactory")
    public void consumeUserCreated(String message) throws JsonProcessingException {
        User user = objectMapper.readValue(message, User.class);

        // Insert a default payment method
        Payment defaultPayment = new Payment();
        defaultPayment.setId(UUID.randomUUID().toString());
        defaultPayment.setUserId(user.getId());
        defaultPayment.setActivated(false);

        paymentRepository.save(defaultPayment);
        log.info("Default payment method {} created for user", defaultPayment.getId());
    }

    public void activatePayment(String paymentId) throws JsonProcessingException {
        Payment payment = paymentRepository.findById(paymentId).get();
        payment.setActivated(true);
        paymentRepository.save(payment);    // Save in database

        kafkaTemplate.send(TOPIC_PAYMENT_ACTIVATED, objectMapper.writeValueAsString(payment));
        log.info("Payment activated for user {}", payment.getUserId());
    }
}
