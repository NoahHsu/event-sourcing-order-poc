package org.example.event.sourcing.order.poc.command.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.command.order.producer.OrderEventProducer;
import org.example.event.sourcing.order.poc.common.annotation.LogInfo;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEventName.COMPLETED;
import static org.example.event.sourcing.order.poc.common.model.event.OrderEventName.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
@LogInfo
public class OrderService {

    private final OrderEventProducer orderEventProducer;

    public Order createOrder(Order order) {
        boolean isSuccess = orderEventProducer.create(new OrderEvent(order.id(), CREATED, Instant.now(), Instant.now()));
        if (isSuccess) {
            return order;
        } else {
            log.warn("createOrder fail", order);
            throw new RuntimeException("create event fail");
        }
    }

    public String completeOrder(String id) {
        boolean isSuccess = orderEventProducer.create(new OrderEvent(id, COMPLETED, Instant.now(), Instant.now()));
        if (isSuccess) {
            return "OK";
        } else {
            throw new RuntimeException("complete event fail");
        }
    }

}
