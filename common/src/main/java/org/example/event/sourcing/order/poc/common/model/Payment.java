package org.example.event.sourcing.order.poc.common.model;

import lombok.Builder;

public record Payment(String id, String paymentMethod) {

    @Builder
    public Payment {
    }

}
