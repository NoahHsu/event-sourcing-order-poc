package org.example.event.sourcing.order.poc.command.shipment.producer;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.modules.event.model.ShipmentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.example.event.sourcing.order.poc.modules.event.model.ShipmentEvent.SHIPMENT_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
@Observed
public class ShipmentEventProducer {

    private final KafkaTemplate<String, ShipmentEvent> kafkaTemplate;

    public Boolean create(ShipmentEvent shipmentEvent) {
        log.info("Attempting to log {} to topic {}.", shipmentEvent, SHIPMENT_TOPIC);
        final String key = shipmentEvent.id();
        try {
            SendResult<String, ShipmentEvent> result = kafkaTemplate
                    .send(SHIPMENT_TOPIC, key, shipmentEvent)
                    .get(10, TimeUnit.SECONDS);
            onSuccess(result);
            return true;
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            onFailure(e);
            return false;
        }
    }

    private void onSuccess(final SendResult<String, ShipmentEvent> result) {
        log.info("Order '{}' has been written to topic-partition {}-{} with ingestion timestamp {}.",
                result.getProducerRecord().key(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().timestamp());
    }

    private void onFailure(final Throwable t) {
        log.warn("Unable to write Order to topic {}.", SHIPMENT_TOPIC, t);
    }

}
