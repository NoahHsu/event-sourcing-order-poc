package org.example.event.sourcing.order.poc.handler.shipment.domain.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent;
import org.example.event.sourcing.order.poc.handler.shipment.domain.ShipmentEventHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShipmentEventHandlerImpl implements ShipmentEventHandler {

    @Override
    public CompletableFuture<Void> onEvent(ShipmentEvent event) {
        return CompletableFuture.runAsync(() -> {
            switch (event.eventName()) {
                case CREATED:
                case PREPARING:
                case RECEIVED:
                case PROCESSED:
                case ARRIVED_DC:
                case DELIVERING:
                case DEPARTED_DC:
                    handleShipmentEvent(event);
                default:
                    throw new RuntimeException("unsupported event");
            }
        });
    }

    private void handleShipmentEvent(ShipmentEvent event) {
        log.info(event.toString());
    }

}
