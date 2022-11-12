package org.example.event.sourcing.order.poc.common.model.event;

import lombok.Builder;

import java.time.Instant;

public record PaymentEvent(String id, PaymentEventName eventName, Instant createdDate, Instant updatedDate) {

    public static final String PAYMENT_TOPIC = "PAYMENT";

    public static final String PAYMENT_STATUS_GROUP_ID_PREFIX = "PAYMENT-STATUS-";
    public static final String PAYMENT_LOG_GROUP_ID_PREFIX = "PAYMENT-LOG-";

    @Builder
    public PaymentEvent {
    }

}
