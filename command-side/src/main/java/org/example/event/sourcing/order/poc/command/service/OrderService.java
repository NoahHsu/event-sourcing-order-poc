package org.example.event.sourcing.order.poc.command.service;

import org.example.event.sourcing.order.poc.common.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public Order createOrder(Order order) {
        return new Order("test");
    }

}
