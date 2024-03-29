package org.example.event.sourcing.order.poc.modules.event.model;

import lombok.Builder;

import java.time.Instant;

public record PaymentEvent(String id, PaymentEventName eventName, String paymentMethod , Instant createdDate, Instant updatedDate) {

    public static final String PAYMENT_TOPIC = "PAYMENT";

    public static final int PAYMENT_TOPIC_PARTITION = 3;

    public static final String PAYMENT_EVENT_HANDLER_GROUP_ID = "PAYMENT-HANDLER";

    public static final String PAYMENT_STATUS_GROUP_ID_PREFIX = "PAYMENT-STATUS-";

    @Builder
    public PaymentEvent {
    }

}
