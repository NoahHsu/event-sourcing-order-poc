package org.example.event.sourcing.order.poc.query.shipment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.query.shipment.domain.entity.ShipmentRecord;
import org.example.event.sourcing.order.poc.query.shipment.domain.repo.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipmentReadService {

    private final ShipmentRepository shipmentRepository;

    public List<ShipmentRecord> getShipments() {
        return shipmentRepository.findAll();
    }

    public ShipmentRecord getShipment(String shipmentId) {
        return shipmentRepository.findById(shipmentId).orElseThrow();
    }

}
