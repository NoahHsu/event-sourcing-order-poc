package org.example.event.sourcing.order.poc.command.shipment.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.event.model.ShipmentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import static org.example.event.sourcing.order.poc.event.model.ShipmentEvent.SHIPMENT_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventProducer {

    private final KafkaTemplate<String, ShipmentEvent> kafkaTemplate;

    public Boolean create(ShipmentEvent shipmentEvent) {
        log.info("Attempting to log {} to topic {}.", shipmentEvent, SHIPMENT_TOPIC);
        return kafkaTemplate.executeInTransaction(operations -> {
            final String key = shipmentEvent.id();
            operations
                    .send(SHIPMENT_TOPIC, key, shipmentEvent)
                    .thenAccept(this::onSuccess)
                    .exceptionally(this::onFailure);
            return true;
        });
    }

    private void onSuccess(final SendResult<String, ShipmentEvent> result) {
        log.info("Order '{}' has been written to topic-partition {}-{} with ingestion timestamp {}.",
                result.getProducerRecord().key(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().timestamp());
    }

    private Void onFailure(final Throwable t) {
        log.warn("Unable to write Order to topic {}.", SHIPMENT_TOPIC, t);
        return null;
    }

}
