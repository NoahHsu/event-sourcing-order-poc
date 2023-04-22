package org.example.event.sourcing.order.poc.event.model;

public enum PaymentEventName {
    CREATED,

    INVALID,
    VALIDATED,

    UNAUTHORIZED,
    AUTHORIZED,
    CANCELLED,

    SETTLED
}
