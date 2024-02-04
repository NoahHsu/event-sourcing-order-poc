package org.example.event.sourcing.order.poc.query.shipment.consumer;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.modules.event.model.ShipmentEvent;
import org.example.event.sourcing.order.poc.query.shipment.domain.handler.ShipmentRecordHandler;
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

import static org.example.event.sourcing.order.poc.modules.event.model.OrderEvent.ORDER_TOPIC;
import static org.example.event.sourcing.order.poc.modules.event.model.ShipmentEvent.SHIPMENT_STATUS_GROUP_ID_PREFIX;
import static org.example.event.sourcing.order.poc.modules.event.model.ShipmentEvent.SHIPMENT_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
@Observed
public class ShipmentConsumer {

    private final ShipmentRecordHandler shipmentRecordHandler;

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
    @KafkaListener(topics = SHIPMENT_TOPIC, groupId = SHIPMENT_STATUS_GROUP_ID_PREFIX + "#{ T(java.util.UUID).randomUUID().toString() }")
    public void shipmentEventListener(ShipmentEvent shipmentEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", shipmentEvent);
        try {
            shipmentRecordHandler.onEvent(shipmentEvent);
            ack.acknowledge();
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", shipmentEvent);
            throw e;
        }
    }

    @DltHandler
    public void processMessage(ShipmentEvent message) {
        log.error("{} DltHandler processMessage = {}", ORDER_TOPIC, message);
    }
}
