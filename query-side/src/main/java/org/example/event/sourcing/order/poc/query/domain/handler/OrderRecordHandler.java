package org.example.event.sourcing.order.poc.query.domain.handler;

import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;

import java.util.concurrent.CompletableFuture;

public interface OrderRecordHandler {
    CompletableFuture<Void> onEvent(OrderEvent event);
}
