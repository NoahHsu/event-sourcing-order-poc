package org.example.event.sourcing.order.poc.command.order.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public Boolean create(OrderEvent order) {
        log.info("Attempting to log {} to topic {}.", order, ORDER_TOPIC);
        return kafkaTemplate.executeInTransaction(operations -> {
            final String key = order.id();
            operations
                    .send(ORDER_TOPIC, key, order)
                    .addCallback(this::onSuccess, this::onFailure);
            return true;
        });
    }

    private void onSuccess(final SendResult<String, OrderEvent> result) {
        log.info("Order '{}' has been written to topic-partition {}-{} with ingestion timestamp {}.",
                result.getProducerRecord().key(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().timestamp());
    }

    private void onFailure(final Throwable t) {
        log.warn("Unable to write Order to topic {}.", ORDER_TOPIC, t);
    }


}
