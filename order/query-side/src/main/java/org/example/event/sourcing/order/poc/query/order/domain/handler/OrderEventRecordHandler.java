package org.example.event.sourcing.order.poc.query.order.domain.handler;

import org.example.event.sourcing.order.poc.event.model.OrderEvent;

import java.util.concurrent.CompletableFuture;

public interface OrderEventRecordHandler {
    void onEvent(OrderEvent event);
    
}
