package org.example.event.sourcing.order.poc.handler.payment.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;
import org.example.event.sourcing.order.poc.handler.payment.domain.PaymentEventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.event.sourcing.order.poc.common.model.event.PaymentEvent.PAYMENT_EVENT_HANDLER_GROUP_ID;
import static org.example.event.sourcing.order.poc.common.model.event.PaymentEvent.PAYMENT_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentEventHandler paymentEventHandler;

    @KafkaListener(topics = PAYMENT_TOPIC, groupId = PAYMENT_EVENT_HANDLER_GROUP_ID)
    public void paymentEventListener(PaymentEvent paymentEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", paymentEvent);
        try {
            paymentEventHandler.onEvent(paymentEvent)
                    .thenRun(ack::acknowledge);
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", paymentEvent);
        }
    }
}