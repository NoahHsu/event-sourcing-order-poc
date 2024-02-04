package org.example.event.sourcing.order.poc.modules.event.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.event.sourcing.order.poc.modules.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.modules.event.model.PaymentEvent;
import org.example.event.sourcing.order.poc.modules.event.model.ShipmentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration(proxyBeanMethods = false)
public class TopicConfig {

    @Bean
    public NewTopic orderEvent() {
        return TopicBuilder.name(OrderEvent.ORDER_TOPIC)
                .partitions(OrderEvent.ORDER_TOPIC_PARTITION)
                .config(org.apache.kafka.common.config.TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Long.MAX_VALUE))
                .compact()
                .build();
    }
    @Bean
    public NewTopic paymentEvent() {
        return TopicBuilder.name(PaymentEvent.PAYMENT_TOPIC)
                .partitions(PaymentEvent.PAYMENT_TOPIC_PARTITION)
                .config(org.apache.kafka.common.config.TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Long.MAX_VALUE))
                .compact()
                .build();
    }

    @Bean
    public NewTopic shipmentEvent() {
        return TopicBuilder.name(ShipmentEvent.SHIPMENT_TOPIC)
                .partitions(ShipmentEvent.SHIPMENT_TOPIC_PARTITION)
                .config(org.apache.kafka.common.config.TopicConfig.RETENTION_MS_CONFIG, String.valueOf(Long.MAX_VALUE))
                .compact()
                .build();
    }

}