package org.example.event.sourcing.order.poc.query.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderEventRecord;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderRecord;
import org.example.event.sourcing.order.poc.query.order.service.OrderReadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final OrderReadService orderReadService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderRecord> getOrders() {
        return orderReadService.getOrder();
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderRecord getOrder(@PathVariable String orderId) {
        return orderReadService.getOrder(orderId);
    }

    @GetMapping("/{orderId}/log")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderEventRecord> getOrderLog(@PathVariable String orderId) {
        return orderReadService.getOrderLog(orderId);
    }

}
