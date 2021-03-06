package org.example.event.sourcing.order.poc.query.service;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.query.domain.entity.OrderEventRecord;
import org.example.event.sourcing.order.poc.query.domain.entity.OrderRecord;
import org.example.event.sourcing.order.poc.query.domain.repo.OrderEventRepository;
import org.example.event.sourcing.order.poc.query.domain.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderReadService {
    private final OrderRepository orderRepository;
    private final OrderEventRepository orderEventRepository;

    public List<OrderRecord> getOrder() {
        return orderRepository.findAll();
    }

    public OrderRecord getOrder(String orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    public List<OrderEventRecord> getOrderLog(String orderId) {
        return orderEventRepository.findByOrderId(orderId);
    }
}
