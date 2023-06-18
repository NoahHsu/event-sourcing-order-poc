package org.example.event.sourcing.order.poc.query.payment.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.event.model.PaymentEvent;
import org.example.event.sourcing.order.poc.query.payment.domain.handler.PaymentRecordHandler;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.example.event.sourcing.order.poc.event.model.PaymentEvent.PAYMENT_STATUS_GROUP_ID_PREFIX;
import static org.example.event.sourcing.order.poc.event.model.PaymentEvent.PAYMENT_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentRecordHandler paymentRecordHandler;

    @RetryableTopic(kafkaTemplate = "kafkaTemplate",
            exclude = {DeserializationException.class,
                    MessageConversionException.class,
                    ConversionException.class,
                    MethodArgumentResolutionException.class,
                    NoSuchMethodException.class,
                    ClassCastException.class},
            attempts = "4",
            backoff = @Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000)
    )
    @KafkaListener(topics = PAYMENT_TOPIC, groupId = PAYMENT_STATUS_GROUP_ID_PREFIX + "#{ T(java.util.UUID).randomUUID().toString() }")
    @Transactional
    public void paymentEventListener(PaymentEvent paymentEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", paymentEvent);
        try {
            paymentRecordHandler.onEvent(paymentEvent);
            ack.acknowledge();
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", paymentEvent, e);
        }
    }

    @DltHandler
    public void processMessage(PaymentEvent message) {
        log.error("{} DltHandler processMessage = {}", PAYMENT_TOPIC, message);
    }


}
