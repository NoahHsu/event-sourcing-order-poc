package org.example.event.sourcing.order.poc.command.order.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.command.order.service.OrderService;
import org.example.event.sourcing.order.poc.common.annotation.LogInfo;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
@Observed
@LogInfo
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PostMapping("/complete")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> completeOrder(@RequestBody Order order) {
        return Map.of("result", orderService.completeOrder(order.id()));
    }

}
