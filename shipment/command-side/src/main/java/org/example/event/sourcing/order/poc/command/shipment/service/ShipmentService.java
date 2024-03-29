package org.example.event.sourcing.order.poc.command.shipment.service;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.command.shipment.producer.ShipmentEventProducer;
import org.example.event.sourcing.order.poc.modules.common.model.Shipment;
import org.example.event.sourcing.order.poc.modules.event.model.ShipmentEvent;
import org.example.event.sourcing.order.poc.modules.event.model.ShipmentEventName;
import org.example.event.sourcing.order.poc.modules.observation.annotation.LogInfo;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.example.event.sourcing.order.poc.modules.event.model.ShipmentEventName.*;

@Service
@RequiredArgsConstructor
@LogInfo
public class ShipmentService {

    private final ShipmentEventProducer shipmentEventProducer;

    public Shipment createShipment(Shipment shipment) {
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipment.id(), shipment.shipmentMethod(), CREATED));
        if (isSuccess) {
            return shipment;
        } else {
            throw new RuntimeException("create shipment event fail");
        }
    }

    private ShipmentEvent getShipmentEvent(String id, String shipmentMethod, ShipmentEventName event) {
        return ShipmentEvent.builder()
                .id(id)
                .shipmentMethod(shipmentMethod)
                .eventName(event)
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();
    }

    public boolean prepareShipment(String shipmentId) {
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, null, PREPARING));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("prepare shipment event fail");
        }
    }

    public boolean sellerSendShipment(String shipmentId) {
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, null, PROCESSED));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

    public boolean dcReceiveShipment(String shipmentId) {
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, null, ARRIVED_DC));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

    public boolean dcSendShipment(String shipmentId) {
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, null, DEPARTED_DC));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

    public boolean deliverShipment(String shipmentId) {
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, null, DELIVERING));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

    public boolean buyerReceiveShipment(String shipmentId) {
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, null, RECEIVED));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

}
