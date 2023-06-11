package org.example.event.sourcing.order.poc.event.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static org.example.event.sourcing.order.poc.event.model.OrderEvent.*;
import static org.example.event.sourcing.order.poc.event.model.PaymentEvent.PAYMENT_TOPIC;
import static org.example.event.sourcing.order.poc.event.model.PaymentEvent.PAYMENT_TOPIC_PARTITION;
import static org.example.event.sourcing.order.poc.event.model.ShipmentEvent.SHIPMENT_TOPIC;
import static org.example.event.sourcing.order.poc.event.model.ShipmentEvent.SHIPMENT_TOPIC_PARTITION;

@Configuration(proxyBeanMethods = false)
public class TopicConfig {

    @Bean
    public NewTopic orderEvent() {
        return TopicBuilder.name(ORDER_TOPIC)
                .partitions(ORDER_TOPIC_PARTITION)
                .config(org.apache.kafka.common.config.TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Long.MAX_VALUE))
                .compact()
                .build();
    }
    @Bean
    public NewTopic paymentEvent() {
        return TopicBuilder.name(PAYMENT_TOPIC)
                .partitions(PAYMENT_TOPIC_PARTITION)
                .config(org.apache.kafka.common.config.TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Long.MAX_VALUE))
                .compact()
                .build();
    }

    @Bean
    public NewTopic shipmentEvent() {
        return TopicBuilder.name(SHIPMENT_TOPIC)
                .partitions(SHIPMENT_TOPIC_PARTITION)
                .config(org.apache.kafka.common.config.TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Long.MAX_VALUE))
                .compact()
                .build();
    }

}