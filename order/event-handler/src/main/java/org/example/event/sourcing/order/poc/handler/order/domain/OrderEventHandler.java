package org.example.event.sourcing.order.poc.handler.order.domain;

import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;

import java.util.concurrent.CompletableFuture;

public interface OrderEventHandler {

    CompletableFuture<Void> onEvent(OrderEvent event);

}
