package org.example.event.sourcing.order.poc.command.order.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.example.event.sourcing.order.poc.command.order.producer.OrderEventProducer;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEventName.COMPLETED;
import static org.example.event.sourcing.order.poc.common.model.event.OrderEventName.CREATED;

@Service
@RequiredArgsConstructor
public class OrderV2Service {

    private final OrderEventProducer orderEventProducer;

    public Order createOrder(Order order) {
        boolean isSuccess = orderEventProducer.create(new OrderEvent(order.id(), CREATED, Instant.now(), Instant.now()));
        if (isSuccess) {
            return order;
        } else {
            throw new RuntimeException("v2 create event fail");
        }
    }

    public String completeOrder(String id) {
        boolean isSuccess = orderEventProducer.create(new OrderEvent(id, COMPLETED, Instant.now(), Instant.now()));
        if (isSuccess) {
            return "v2 OK";
        } else {
            throw new RuntimeException("v2 complete event fail");
        }
    }

}
