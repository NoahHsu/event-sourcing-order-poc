package org.example.event.sourcing.order.poc.query.service;

import org.example.event.sourcing.order.poc.common.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderReadService {
    public Order getOrder(){
        return new Order("read model");
    }
}
