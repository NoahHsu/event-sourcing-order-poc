package org.example.event.sourcing.order.poc.query.shipment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent.SHIPMENT_TOPIC;
import static org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent.SHIPMENT_TOPIC_PARTITION;

@Configuration(proxyBeanMethods = false)
public class TopicConfig {

    @Bean
    public NewTopic shipmentEvent() {
        return TopicBuilder.name(SHIPMENT_TOPIC)
                .partitions(SHIPMENT_TOPIC_PARTITION)
                .build();
    }

}