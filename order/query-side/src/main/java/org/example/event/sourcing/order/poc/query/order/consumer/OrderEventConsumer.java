package org.example.event.sourcing.order.poc.query.order.consumer;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderEventRecordHandler;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderRecordHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static org.example.event.sourcing.order.poc.event.model.OrderEvent.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Observed
public class OrderEventConsumer {

    private final OrderRecordHandler orderRecordHandler;

    private final OrderEventRecordHandler orderEventRecordHandler;

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_STATUS_GROUP_ID_PREFIX + "#{ T(java.util.UUID).randomUUID().toString() }")
    public void orderEventListener(OrderEvent orderEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", orderEvent);
        try {
            orderRecordHandler.onEvent(orderEvent).join();
            ack.acknowledge();
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", orderEvent, e);
        }
    }

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_LOG_GROUP_ID_PREFIX + "#{ T(java.util.UUID).randomUUID().toString() }")
    public void orderEventRecordListener(OrderEvent orderEvent, Acknowledgment ack) {
        log.info("log handler receive data = {}", orderEvent);
        try {
            orderEventRecordHandler.onEvent(orderEvent).join();
            ack.acknowledge();
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", orderEvent, e);
        }
    }

}