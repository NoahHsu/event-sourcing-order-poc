package org.example.event.sourcing.order.poc.query.domain.entity;

public enum OrderStatus {
    CREATED, PREPARING, IN_LOGISTICS, PICKED_UP, FINISHED;
}
