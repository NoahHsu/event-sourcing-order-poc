package org.example.event.sourcing.order.poc.command.producer;

import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_TOPIC;

@Component
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Autowired
    public OrderEventProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public Boolean create(OrderEvent order) {
        log.info("Attempting to log {} to topic {}.", order, ORDER_TOPIC);
        Boolean result = kafkaTemplate.executeInTransaction(operations -> {
            final String key = order.id();
            operations
                    .send(ORDER_TOPIC, key, order)
                    .addCallback(this::onSuccess, this::onFailure);
            return true;
        });
        return result;
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
