package org.example.event.sourcing.order.poc.client.order.model;

import lombok.Builder;

import java.time.Instant;

public record V1Order(
        V1OrderStatus status,
        Instant createdDate,
        Instant updatedDate
) {

    @Builder
    public V1Order {
    }
    
}
