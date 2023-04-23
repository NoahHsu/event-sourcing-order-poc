package org.example.event.sourcing.order.poc.command.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.client.order.OrderQueryClient;
import org.example.event.sourcing.order.poc.client.order.model.V1Order;
import org.example.event.sourcing.order.poc.client.order.model.V1OrderStatus;
import org.example.event.sourcing.order.poc.command.order.producer.OrderEventProducer;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.example.event.sourcing.order.poc.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.observation.annotation.LogInfo;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.example.event.sourcing.order.poc.event.model.OrderEventName.COMPLETED;
import static org.example.event.sourcing.order.poc.event.model.OrderEventName.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
@LogInfo
public class OrderService {

    private final OrderQueryClient orderQueryClient;
    private final OrderEventProducer orderEventProducer;

    public Order createOrder(Order order) {
        boolean isSuccess = orderEventProducer.create(new OrderEvent(order.id(), CREATED, Instant.now()));
        if (isSuccess) {
            return order;
        } else {
            log.warn("createOrder fail", order);
            throw new RuntimeException("create event fail");
        }
    }

    public String completeOrder(String id) {
        V1Order result = orderQueryClient.get(id);
        if (result.status() == V1OrderStatus.CREATED) {
            orderEventProducer.create(new OrderEvent(id, COMPLETED, Instant.now()));
            return "OK";
        } else {
            throw new RuntimeException("order(id = {}) is not in right status.");
        }
    }

}
