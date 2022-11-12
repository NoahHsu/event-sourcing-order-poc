package org.example.event.sourcing.order.poc.command.payment.producer;

import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import static org.example.event.sourcing.order.poc.common.model.event.PaymentEvent.PAYMENT_TOPIC;

@Component
public class PaymentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Autowired
    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Boolean create(PaymentEvent paymentEvent) {
        log.info("Attempting to log {} to topic {}.", paymentEvent, PAYMENT_TOPIC);
        Boolean result = kafkaTemplate.executeInTransaction(operations -> {
            final String key = paymentEvent.id();
            operations
                    .send(PAYMENT_TOPIC, key, paymentEvent)
                    .addCallback(this::onSuccess, this::onFailure);
            return true;
        });
        return result;
    }

    private void onSuccess(final SendResult<String, PaymentEvent> result) {
        log.info("Order '{}' has been written to topic-partition {}-{} with ingestion timestamp {}.",
                result.getProducerRecord().key(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().timestamp());
    }

    private void onFailure(final Throwable t) {
        log.warn("Unable to write Order to topic {}.", PAYMENT_TOPIC, t);
    }


}
