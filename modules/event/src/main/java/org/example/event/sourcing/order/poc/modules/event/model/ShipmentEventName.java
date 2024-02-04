package org.example.event.sourcing.order.poc.modules.event.model;

public enum ShipmentEventName {
    CREATED,

    PREPARING,
    PROCESSED,
    ARRIVED_DC,
    DEPARTED_DC,

    DELIVERING,
    RECEIVED
}
