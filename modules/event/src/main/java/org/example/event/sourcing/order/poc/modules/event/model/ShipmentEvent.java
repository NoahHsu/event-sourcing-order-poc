package org.example.event.sourcing.order.poc.modules.event.model;

import lombok.Builder;

import java.time.Instant;

public record ShipmentEvent(String id, String shipmentMethod, ShipmentEventName eventName, Instant createdDate, Instant updatedDate) {

    public static final String SHIPMENT_TOPIC = "SHIPMENT";
    public static final int SHIPMENT_TOPIC_PARTITION = 3;

    public static final String SHIPMENT_STATUS_GROUP_ID_PREFIX = "SHIPMENT-STATUS-";

    public static final String SHIPMENT_EVENT_HANDLER_GROUP_ID = "SHIPMENT-HANDLER";
    public static final String SHIPMENT_LOG_GROUP_ID_PREFIX = "SHIPMENT-LOG-";

    @Builder
    public ShipmentEvent {
    }
}
