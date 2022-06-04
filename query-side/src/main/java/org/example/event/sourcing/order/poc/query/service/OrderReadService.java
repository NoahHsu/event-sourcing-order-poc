package org.example.event.sourcing.order.poc.query.service;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.query.domain.entity.OrderRecord;
import org.example.event.sourcing.order.poc.query.domain.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderReadService {
    private final OrderRepository orderRepository;

    public List<OrderRecord> getOrder() {
        return orderRepository.findAll();
    }
}
