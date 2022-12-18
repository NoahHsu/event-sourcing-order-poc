package org.example.event.sourcing.order.poc.command.shipment.controller;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.command.shipment.service.ShipmentService;
import org.example.event.sourcing.order.poc.command.shipment.service.ShipmentV2Service;
import org.example.event.sourcing.order.poc.common.model.Shipment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v2/shipments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ShipmentV2Controller {

    private final ShipmentV2Service shipmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Shipment createShipment(@RequestBody Shipment shipment) {
        return shipmentService.createShipment(shipment);
    }

    @PostMapping("{id}/prepare")
    @ResponseStatus(HttpStatus.OK)
    public String prepareShipment(@PathVariable String id) {
        return String.valueOf(shipmentService.prepareShipment(id));
    }

    @PostMapping("{id}/seller-send")
    @ResponseStatus(HttpStatus.OK)
    public String sellerSendShipment(@PathVariable String id) {
        return String.valueOf(shipmentService.sellerSendShipment(id));
    }

    @PostMapping("{id}/dc-receive")
    @ResponseStatus(HttpStatus.OK)
    public String dcReceiveShipment(@PathVariable String id) {
        return String.valueOf(shipmentService.dcReceiveShipment(id));
    }

    @PostMapping("{id}/dc-send")
    @ResponseStatus(HttpStatus.OK)
    public String dcSendShipment(@PathVariable String id) {
        return String.valueOf(shipmentService.dcSendShipment(id));
    }

    @PostMapping("{id}/deliver")
    @ResponseStatus(HttpStatus.OK)
    public String deliverShipment(@PathVariable String id) {
        return String.valueOf(shipmentService.deliverShipment(id));
    }

    @PostMapping("{id}/buyer-receive")
    @ResponseStatus(HttpStatus.OK)
    public String buyerReceiveShipment(@PathVariable String id) {
        return String.valueOf(shipmentService.buyerReceiveShipment(id));
    }

}
