package org.example.event.sourcing.order.poc.handler.payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static org.example.event.sourcing.order.poc.common.model.event.PaymentEvent.PAYMENT_TOPIC;
import static org.example.event.sourcing.order.poc.common.model.event.PaymentEvent.PAYMENT_TOPIC_PARTITION;

@Configuration(proxyBeanMethods = false)
public class TopicConfig {

    @Bean
    public NewTopic paymentEvent() {
        return TopicBuilder.name(PAYMENT_TOPIC)
                .partitions(PAYMENT_TOPIC_PARTITION)
                .build();
    }

}