package com.saraiyakush.microservices.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean activated;

    // This field has nothing to do with User object, but is used by Email Service to indicate a failure and demonstrate how to do rollbacks
    private Boolean failure;
}
