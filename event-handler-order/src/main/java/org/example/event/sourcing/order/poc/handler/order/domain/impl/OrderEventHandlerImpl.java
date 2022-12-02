package org.example.event.sourcing.order.poc.handler.order.domain.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.example.event.sourcing.order.poc.handler.order.domain.OrderEventHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventHandlerImpl implements OrderEventHandler {

    @Override
    public CompletableFuture<Void> onEvent(OrderEvent event) {
        return CompletableFuture.runAsync(() -> {
            switch (event.eventName()) {
                case CREATED:
                    handleOrderEvent(event);
                default:
                    throw new RuntimeException("unsupported event");
            }
        });
    }

    private void handleOrderEvent(OrderEvent event) {
        log.info(event.toString());
    }

}
