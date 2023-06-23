package org.example.event.sourcing.order.poc.query.shipment.domain.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.event.model.ShipmentEvent;
import org.example.event.sourcing.order.poc.observation.annotation.LogInfo;
import org.example.event.sourcing.order.poc.query.shipment.domain.entity.ShipmentRecord;
import org.example.event.sourcing.order.poc.query.shipment.domain.handler.ShipmentRecordHandler;
import org.example.event.sourcing.order.poc.query.shipment.domain.repo.ShipmentRepository;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@LogInfo
public class ShipmentRecordHandlerImpl implements ShipmentRecordHandler {

    private final ShipmentRepository shipmentRepository;

    @Override
    public void onEvent(ShipmentEvent event) {
        switch (event.eventName()) {
            case CREATED:
                createShipment(event);
                break;
            case PREPARING:
            case RECEIVED:
            case PROCESSED:
            case ARRIVED_DC:
            case DELIVERING:
            case DEPARTED_DC:
                updateShipment(event);
                break;
            default:
                throw new RuntimeException("unsupported event");
        }
    }

    private void createShipment(ShipmentEvent event) {
        log.info("create shipment id = {}.", event.id());
        ShipmentRecord entity = ShipmentRecord.builder()
                .shipmentId(event.id())
                .shipmentMethod(event.shipmentMethod())
                .state(event.eventName())
                .createDate(event.createdDate())
                .updateDate(event.updatedDate())
                .build();
        shipmentRepository.save(entity);
        log.info("save shipment success");
    }

    private void updateShipment(ShipmentEvent event) {
        log.info("update shipment by = {}.", event);
        shipmentRepository.findById(event.id()).ifPresentOrElse(
                shipmentRecord -> {
                    shipmentRecord.setState(event.eventName());
                    shipmentRepository.save(shipmentRecord);
                    log.info("update success. id = {}", shipmentRecord.getShipmentId());
                },
                () -> log.warn("shipment not found bby id = {}", event.id()));
    }

}
