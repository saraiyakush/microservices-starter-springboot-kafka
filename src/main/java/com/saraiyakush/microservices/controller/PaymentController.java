package com.saraiyakush.microservices.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saraiyakush.microservices.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payments/{id}")
    public ResponseEntity<String> activatePayment(@PathVariable String id) throws JsonProcessingException {
        paymentService.activatePayment(id);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
