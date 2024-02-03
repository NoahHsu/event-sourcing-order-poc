package org.example.event.sourcing.order.poc.command.payment.producer;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.modules.event.model.PaymentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.example.event.sourcing.order.poc.modules.event.model.PaymentEvent.PAYMENT_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
@Observed
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public Boolean create(PaymentEvent paymentEvent) {
        log.info("Attempting to log {} to topic {}.", paymentEvent, PAYMENT_TOPIC);
        final String key = paymentEvent.id();
        try {
            SendResult<String, PaymentEvent> result = kafkaTemplate
                    .send(PAYMENT_TOPIC, key, paymentEvent)
                    .get(10, TimeUnit.SECONDS);
            onSuccess(result);
            return true;
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            onFailure(e);
            return false;
        }
    }

    private void onSuccess(final SendResult<String, PaymentEvent> result) {
        log.info("Payment '{}' has been written to topic-partition {}-{} with ingestion timestamp {}.",
                result.getProducerRecord().key(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().timestamp());
    }

    private void onFailure(final Throwable t) {
        log.warn("Unable to write Payment to topic {}.", PAYMENT_TOPIC, t);
    }

}
