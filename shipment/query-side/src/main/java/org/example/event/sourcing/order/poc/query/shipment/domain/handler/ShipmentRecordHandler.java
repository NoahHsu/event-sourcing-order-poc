package org.example.event.sourcing.order.poc.query.shipment.domain.handler;

import org.example.event.sourcing.order.poc.event.model.ShipmentEvent;

import java.util.concurrent.CompletableFuture;

public interface ShipmentRecordHandler {

    CompletableFuture<Void> onEvent(ShipmentEvent event);

}
