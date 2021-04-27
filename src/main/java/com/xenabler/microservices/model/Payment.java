package com.xenabler.microservices.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    private String id;
    private String userId;
    private String paymentMethod;
    private Boolean activated;
}