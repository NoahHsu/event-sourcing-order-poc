package org.example.event.sourcing.order.poc.query.order.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderEventRecordHandler;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderRecordHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final OrderRecordHandler orderRecordHandler;

    private final OrderEventRecordHandler orderEventRecordHandler;

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_STATUS_GROUP_ID_PREFIX +"#{ T(java.util.UUID).randomUUID().toString() }")
    public void OrderEventListener(OrderEvent orderEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", orderEvent);
        try {
            orderRecordHandler.onEvent(orderEvent)
                    .thenRun(ack::acknowledge);
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", orderEvent, e);
        }
    }

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_LOG_GROUP_ID_PREFIX +"#{ T(java.util.UUID).randomUUID().toString() }")
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