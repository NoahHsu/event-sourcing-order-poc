package org.example.event.sourcing.order.poc.command.payment.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import static org.example.event.sourcing.order.poc.common.model.event.PaymentEvent.PAYMENT_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public Boolean create(PaymentEvent paymentEvent) {
        log.info("Attempting to log {} to topic {}.", paymentEvent, PAYMENT_TOPIC);
        return kafkaTemplate.executeInTransaction(operations -> {
            final String key = paymentEvent.id();
            operations
                    .send(PAYMENT_TOPIC, key, paymentEvent)
                    .thenAccept(this::onSuccess)
                    .exceptionally(this::onFailure);
            return true;
        });
    }

    private void onSuccess(final SendResult<String, PaymentEvent> result) {
        log.info("Payment '{}' has been written to topic-partition {}-{} with ingestion timestamp {}.",
                result.getProducerRecord().key(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().timestamp());
    }

    private Void onFailure(final Throwable t) {
        log.warn("Unable to write Payment to topic {}.", PAYMENT_TOPIC, t);
        return null;
    }

}
