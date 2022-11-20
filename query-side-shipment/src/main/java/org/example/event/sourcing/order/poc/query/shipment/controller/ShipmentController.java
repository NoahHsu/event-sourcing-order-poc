package org.example.event.sourcing.order.poc.query.shipment.controller;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.query.shipment.domain.entity.ShipmentRecord;
import org.example.event.sourcing.order.poc.query.shipment.service.ShipmentReadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/shipments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentReadService shipmentReadService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ShipmentRecord> getShipments() {
        return shipmentReadService.getShipments();
    }

    @GetMapping("/{shipmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ShipmentRecord getShipment(@PathVariable String shipmentId) {
        return shipmentReadService.getShipment(shipmentId);
    }

}
