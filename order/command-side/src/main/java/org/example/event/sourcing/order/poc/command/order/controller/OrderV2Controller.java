package org.example.event.sourcing.order.poc.command.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.command.order.service.OrderService;
import org.example.event.sourcing.order.poc.command.order.service.OrderV2Service;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v2/orders", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class OrderV2Controller {

    private final OrderV2Service orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String completeOrder(@PathVariable String orderId) {
        return orderService.completeOrder(orderId);
    }

}
