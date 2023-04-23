package org.example.event.sourcing.order.poc.handler.order.domain.impl;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.handler.order.domain.OrderEventHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
@Observed
public class OrderEventHandlerImpl implements OrderEventHandler {

    @Override
    public CompletableFuture<Void> onEvent(OrderEvent event) {
        return CompletableFuture.runAsync(() -> {
            switch (event.eventName()) {
                case CREATED:
                    handleOrderEvent(event);
                    break;
                default:
                    throw new RuntimeException("unsupported event");
            }
        });
    }

    private void handleOrderEvent(OrderEvent event) {
        log.info("call some api on {}", event);
    }

}
