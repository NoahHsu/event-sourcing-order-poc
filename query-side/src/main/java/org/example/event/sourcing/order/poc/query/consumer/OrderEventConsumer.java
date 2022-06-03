package org.example.event.sourcing.order.poc.query.consumer;

import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_STATUS_ID;
import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_TOPIC;

@Service
public class OrderEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_STATUS_ID)
    public void OrderEventListener(OrderEvent orderEvent, Acknowledgment ack) {
        log.info("receive data = {}", orderEvent);
        ack.acknowledge();
    }
}