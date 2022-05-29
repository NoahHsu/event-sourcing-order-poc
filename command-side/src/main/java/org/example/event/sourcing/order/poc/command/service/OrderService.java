package org.example.event.sourcing.order.poc.command.service;

import org.example.event.sourcing.order.poc.command.producer.OrderEventProducer;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderEventProducer orderEventProducer;

    @Autowired
    public OrderService(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    public Order createOrder(Order order) {
        Boolean isSuccess = orderEventProducer.create(order);
        if (isSuccess) {
            return order;
        } else {
            throw new RuntimeException("create event fail");
        }
    }

}
