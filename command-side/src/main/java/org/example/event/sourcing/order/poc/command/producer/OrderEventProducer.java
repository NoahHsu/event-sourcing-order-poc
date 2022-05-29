package org.example.event.sourcing.order.poc.command.producer;

import org.example.event.sourcing.order.poc.common.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);

    private static final String ORDER_TOPIC = "ORDER";
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Autowired
    public OrderEventProducer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public Boolean create(Order order) {
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

    private void onSuccess(final SendResult<String, Order> result) {
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
