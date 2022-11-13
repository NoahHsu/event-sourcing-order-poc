package org.example.event.sourcing.order.poc.query.payment.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;
import org.example.event.sourcing.order.poc.query.payment.domain.handler.PaymentRecordHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_STATUS_GROUP_ID_PREFIX;
import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_TOPIC;
import static org.example.event.sourcing.order.poc.common.model.event.PaymentEvent.PAYMENT_STATUS_GROUP_ID_PREFIX;
import static org.example.event.sourcing.order.poc.common.model.event.PaymentEvent.PAYMENT_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentRecordHandler paymentRecordHandler;

    @KafkaListener(topics = PAYMENT_TOPIC, groupId = PAYMENT_STATUS_GROUP_ID_PREFIX +"#{ T(java.util.UUID).randomUUID().toString() }")
    public void paymentEventListener(PaymentEvent paymentEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", paymentEvent);
        try {
            paymentRecordHandler.onEvent(paymentEvent)
                    .thenRun(ack::acknowledge);
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", paymentEvent, e);
        }
    }

}
