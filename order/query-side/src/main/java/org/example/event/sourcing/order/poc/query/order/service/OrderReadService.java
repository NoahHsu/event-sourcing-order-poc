package org.example.event.sourcing.order.poc.query.order.service;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderEventRecord;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderRecord;
import org.example.event.sourcing.order.poc.query.order.domain.repo.OrderEventRepository;
import org.example.event.sourcing.order.poc.query.order.domain.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Observed
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
