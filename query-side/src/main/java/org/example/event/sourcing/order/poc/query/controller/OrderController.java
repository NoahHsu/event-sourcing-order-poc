package org.example.event.sourcing.order.poc.query.controller;

import org.example.event.sourcing.order.poc.common.model.Order;
import org.example.event.sourcing.order.poc.query.service.OrderReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderReadService orderReadService;

    @Autowired
    public OrderController(OrderReadService orderService) {
        this.orderReadService = orderService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Order getOrder() {
        return orderReadService.getOrder();
    }

}
