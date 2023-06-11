package org.example.event.sourcing.order.poc.query.order.domain.handler;

import org.example.event.sourcing.order.poc.event.model.OrderEvent;

import java.util.concurrent.CompletableFuture;

public interface OrderRecordHandler {
    void onEvent(OrderEvent event);

    void onRequeueEvent(OrderEvent event);

}
