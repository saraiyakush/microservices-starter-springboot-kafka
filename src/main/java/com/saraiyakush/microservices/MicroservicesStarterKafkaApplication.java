package com.saraiyakush.microservices;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableKafka    // Detection of methods with @KafkaListener
public class MicroservicesStarterKafkaApplication {

    /**
     * Payment Consumer Factory settings
     * @return ConsumerFactory
     */
    @Bean
    public ConsumerFactory<String, String> paymentConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-service-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Email Consumer Factory settings
     * @return ConsumerFactory
     */
    @Bean
    public ConsumerFactory<String, String> emailConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "email-service-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * User Consumer Factory settings
     * @return ConsumerFactory
     */
    @Bean
    public ConsumerFactory<String, String> userConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "user-service-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Listener container factory for Payment consumers
     * @return ConcurrentKafkaListenerContainerFactory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> paymentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        return factory;
    }

    /**
     * Listener container factory for Email consumers
     * @return ConcurrentKafkaListenerContainerFactory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> emailKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(emailConsumerFactory());
        return factory;
    }

    /**
     * Listener container factory for User consumers
     * @return ConcurrentKafkaListenerContainerFactory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> userKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userConsumerFactory());
        return factory;
    }

    public static void main(String[] args) {
        SpringApplication.run(MicroservicesStarterKafkaApplication.class, args);
    }

}
