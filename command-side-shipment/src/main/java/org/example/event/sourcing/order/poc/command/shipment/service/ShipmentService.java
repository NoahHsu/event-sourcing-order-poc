package org.example.event.sourcing.order.poc.command.shipment.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.example.event.sourcing.order.poc.command.shipment.producer.ShipmentEventProducer;
import org.example.event.sourcing.order.poc.common.model.Shipment;
import org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent;
import org.example.event.sourcing.order.poc.common.model.event.ShipmentEventName;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.example.event.sourcing.order.poc.common.model.event.ShipmentEventName.*;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentEventProducer shipmentEventProducer;

    public Shipment createShipment(Shipment shipment) {
        randomFail();
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipment.id(), shipment.shipmentMethod(), CREATED));
        if (isSuccess) {
            return shipment;
        } else {
            throw new RuntimeException("create shipment event fail");
        }
    }

    private void randomFail() {
        if (RandomUtils.nextBoolean()) {
            throw new RuntimeException("random fail");
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

    private ShipmentEvent getShipmentEvent(String shipmentId, ShipmentEventName event) {
        return ShipmentEvent.builder()
                .id(shipmentId)
                .eventName(event)
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();
    }

    public boolean prepareShipment(String shipmentId) {
        randomFail();
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, PREPARING));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("prepare shipment event fail");
        }
    }

    public boolean sellerSendShipment(String shipmentId) {
        randomFail();
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, PROCESSED));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

    public boolean dcReceiveShipment(String shipmentId) {
        randomFail();
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, ARRIVED_DC));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

    public boolean dcSendShipment(String shipmentId) {
        randomFail();
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, DEPATURED_DC));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

    public boolean deliverShipment(String shipmentId) {
        randomFail();
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, DELIVERING));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

    public boolean buyerReceiveShipment(String shipmentId) {
        randomFail();
        boolean isSuccess = shipmentEventProducer.create(
                getShipmentEvent(shipmentId, RECEIVED));
        if (isSuccess) {
            return true;
        } else {
            throw new RuntimeException("process shipment event fail");
        }
    }

}
