package org.example.event.sourcing.order.poc.query.consumer;

import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.example.event.sourcing.order.poc.query.domain.handler.OrderEventRecordHandler;
import org.example.event.sourcing.order.poc.query.domain.handler.OrderRecordHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.*;

@Service
public class OrderEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @Autowired
    private OrderRecordHandler orderRecordHandler;

    @Autowired
    private OrderEventRecordHandler orderEventRecordHandler;

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_STATUS_ID+"#{ T(java.util.UUID).randomUUID().toString() }")
    public void OrderEventListener(OrderEvent orderEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", orderEvent);
        try {
            orderRecordHandler.onEvent(orderEvent)
                    .thenRun(ack::acknowledge);
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", orderEvent, e);
        }
    }

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_LOG_ID+"#{ T(java.util.UUID).randomUUID().toString() }")
    public void OrderEventRecordListener(OrderEvent orderEvent, Acknowledgment ack) {
        log.info("log handler receive data = {}", orderEvent);
        try {
            orderEventRecordHandler.onEvent(orderEvent)
                    .thenRun(ack::acknowledge);
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", orderEvent, e);
        }
    }

}