package org.example.event.sourcing.order.poc.handler.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_TOPIC;
import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_TOPIC_PARTITION;

@Configuration(proxyBeanMethods = false)
public class TopicConfig {

    @Bean
    public NewTopic orderEvent() {
        return TopicBuilder.name(ORDER_TOPIC)
                .partitions(ORDER_TOPIC_PARTITION)
                .build();
    }

}
