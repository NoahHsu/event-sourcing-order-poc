package org.example.event.sourcing.order.poc.modules.common.model;

import lombok.Builder;

public record Payment(String id, String paymentMethod) {

    @Builder
    public Payment {
    }

}
