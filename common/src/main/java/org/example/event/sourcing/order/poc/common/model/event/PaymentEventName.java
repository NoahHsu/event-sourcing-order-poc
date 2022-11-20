package org.example.event.sourcing.order.poc.common.model.event;

public enum PaymentEventName {
    CREATED,

    INVALID,
    VALIDATED,

    UNAUTHORIZED,
    AUTHORIZED,
    CANCELLED,

    SETTLED
}
