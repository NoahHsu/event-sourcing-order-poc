package org.example.event.sourcing.order.poc.query.controller;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.example.event.sourcing.order.poc.query.domain.entity.OrderEventRecord;
import org.example.event.sourcing.order.poc.query.domain.entity.OrderRecord;
import org.example.event.sourcing.order.poc.query.service.OrderReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
