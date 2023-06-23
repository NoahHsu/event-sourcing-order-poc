package org.example.event.sourcing.order.poc.query.shipment.domain.handler;

import org.example.event.sourcing.order.poc.event.model.ShipmentEvent;

public interface ShipmentRecordHandler {

    void onEvent(ShipmentEvent event);

}
