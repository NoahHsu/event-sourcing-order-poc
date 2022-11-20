package org.example.event.sourcing.order.poc.query.shipment.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent;
import org.example.event.sourcing.order.poc.query.shipment.domain.handler.ShipmentRecordHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent.SHIPMENT_STATUS_GROUP_ID_PREFIX;
import static org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent.SHIPMENT_TOPIC;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShipmentConsumer {

    private final ShipmentRecordHandler shipmentRecordHandler;

    @KafkaListener(topics = SHIPMENT_TOPIC, groupId = SHIPMENT_STATUS_GROUP_ID_PREFIX + "#{ T(java.util.UUID).randomUUID().toString() }")
    public void shipmentEventListener(ShipmentEvent shipmentEvent, Acknowledgment ack) {
        log.info("status handler receive data = {}", shipmentEvent);
        try {
            shipmentRecordHandler.onEvent(shipmentEvent)
                    .thenRun(ack::acknowledge);
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", shipmentEvent);
        }
    }
}
