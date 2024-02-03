package org.example.event.sourcing.order.poc.modules.event.model;

public enum PaymentEventName {
    CREATED,

    INVALID,
    VALIDATED,

    UNAUTHORIZED,
    AUTHORIZED,
    CANCELLED,

    SETTLED
}
