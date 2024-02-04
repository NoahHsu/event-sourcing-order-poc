package org.example.event.sourcing.order.poc.handler.order.consumer;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.modules.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.handler.order.domain.OrderEventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.event.sourcing.order.poc.modules.event.model.OrderEvent.ORDER_EVENT_HANDLER_GROUP_ID;
import static org.example.event.sourcing.order.poc.modules.event.model.OrderEvent.ORDER_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
@Observed
public class OrderConsumer {

    private final OrderEventHandler orderEventHandler;

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_EVENT_HANDLER_GROUP_ID)
    public void orderEventListener(OrderEvent orderEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", orderEvent);
        try {
            orderEventHandler.onEvent(orderEvent)
                    .thenRun(ack::acknowledge);
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", orderEvent);
        }
    }

}