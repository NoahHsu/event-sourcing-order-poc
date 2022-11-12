package org.example.event.sourcing.order.poc.command.order.service;

import org.apache.commons.lang3.RandomUtils;
import org.example.event.sourcing.order.poc.command.order.producer.OrderEventProducer;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEventName.COMPLETED;
import static org.example.event.sourcing.order.poc.common.model.event.OrderEventName.CREATED;

@Service
public class OrderService {

    private final OrderEventProducer orderEventProducer;

    @Autowired
    public OrderService(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    public Order createOrder(Order order) {
        randomFail();
        Boolean isSuccess = orderEventProducer.create(new OrderEvent(order.id(), CREATED, Instant.now(), Instant.now()));
        if (isSuccess) {
            return order;
        } else {
            throw new RuntimeException("create event fail");
        }
    }

    public String completeOrder(String id) {
        randomFail();
        Boolean isSuccess = orderEventProducer.create(new OrderEvent(id, COMPLETED, Instant.now(), Instant.now()));
        if (isSuccess) {
            return "OK";
        } else {
            throw new RuntimeException("complete event fail");
        }
    }

    private void randomFail() {
        if (RandomUtils.nextBoolean()) {
            throw new RuntimeException("random fail");
        }
    }

}
