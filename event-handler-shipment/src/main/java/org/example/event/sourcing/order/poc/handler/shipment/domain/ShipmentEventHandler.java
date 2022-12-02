package org.example.event.sourcing.order.poc.handler.shipment.domain;

import org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent;

import java.util.concurrent.CompletableFuture;

public interface ShipmentEventHandler {

    CompletableFuture<Void> onEvent(ShipmentEvent event);

}
