package org.example.event.sourcing.order.poc.common.model.event;

public enum ShipmentEventName {
    CREATED,

    PREPARING,
    PROCESSED,
    ARRIVED_DC,
    DEPARTED_DC,

    DELIVERING,
    RECEIVED
}
