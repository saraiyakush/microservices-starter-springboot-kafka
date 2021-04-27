## Getting Started

A sample microservices starter project using Spring Boot and Apache Kafka as messaging broker.

## Prerequisites

1.  Kafka installed and running on `localhost:9092`
2.  A Kafka topic created with name `user-created`
3.  A Kafka topic created with name `payment-activated`
4.  A Kafla topic created with name `user-activated`

### Microservices

*   **User Service** (*Producer & Consumer*) &mdash; Creates a user and publishes a message to `user-created` topic. Listens to `payment-activated` topic and activates the user.
*   **Payment Service** (*Producer & Consumer*) &mdash; Listens to `user-created` topic and adds user&apos;s default payment method. Activates a payment method and publishes a message to `payment-activated` topic.
*   **Email Service** (*Consumer*) &mdash; Listens to `user-created` and `user-activated` topics and sends email to the user.

| **Service**         	| **Publishes**                                            	| **Consumes**                                        	|
|---------------------	|----------------------------------------------------------	|-----------------------------------------------------	|
| **User Service**    	| `user-created`</br> `user-activated`</br> `user-deleted` 	| `payment-activated`</br> `create-user-email-failed` 	|
| **Payment Service** 	| `payment-activated`                                      	| `user-created`</br> `user-deleted`                  	|
| **Email Service**   	| `create-user-email-failed`                               	| `user-created`</br> `user-activated`                	|

### Use Cases

1.  User created
    1.  User Service creates a new user and marks the user as `activated=false`.
    2.  Payment Service adds a default payment method of the user and marks it as `activated=false`.
    3.  Email Service sends an email to the user.
2.  Payment activated
    1.  Payment Service activates the user&apos;s payment.
    2.  User Service marks the user as `activated=true`
    3.  Email Service sends an email to the user.

### Tests

Run the application.

1.  Create a user
    1.  `curl -X POST -H "Content-Type: application/json" -d "{\"username\":\"kush.saraiya\",\"firstName\":\"Kush\",\"lastName\":\"Saraiya\"}" http://localhost:8080/users`
    2.  Check console and validate that default payment method is created and email is sent to the user.
    3.  Note the `userId` for next steps.
2.  Activate a payment
    1.  Grab the payment method id from console from previous step and pass it in next step.
    2.  `curl -X POST http://localhost:8080/payments/{id}`
    3.  Check console and validate email is sent to the user. 
3.  Verify user is activated
    1. `curl -X GET http://localhost:8080/users/{id}`

### Project Structure

***NOTE:** Even though the User Service, Payment Service and Email Service are in the same project, in a real world application, these services may be scattered across many servers.*

```
src
├───main
│   ├───java
│   │   └───com
│   │       └───xenabler
│   │           └───microservices
│   │               │   MicroservicesStarterKafkaApplication.java
│   │               │
│   │               ├───controller
│   │               │       PaymentController.java
│   │               │       UserController.java
│   │               │
│   │               ├───model
│   │               │       Payment.java
│   │               │       PaymentRepository.java
│   │               │       User.java
│   │               │       UserRepository.java
│   │               │
│   │               └───service
│   │                       EmailService.java
│   │                       PaymentService.java
│   │                       UserService.java
│   │
│   └───resources
│           application.yml
```


### Reference Documentation

For further reference, please consider the following sections:

* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/2.4.5/reference/htmlsingle/#boot-features-kafka)
