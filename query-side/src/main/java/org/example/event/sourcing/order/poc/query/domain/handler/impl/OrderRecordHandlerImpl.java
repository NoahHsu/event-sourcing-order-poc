package org.example.event.sourcing.order.poc.query.domain.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.example.event.sourcing.order.poc.query.domain.entity.OrderRecord;
import org.example.event.sourcing.order.poc.query.domain.entity.OrderStatus;
import org.example.event.sourcing.order.poc.query.domain.handler.OrderRecordHandler;
import org.example.event.sourcing.order.poc.query.domain.repo.OrderEventRepository;
import org.example.event.sourcing.order.poc.query.domain.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static org.example.event.sourcing.order.poc.query.domain.entity.OrderStatus.CREATED;
import static org.example.event.sourcing.order.poc.query.domain.entity.OrderStatus.PREPARING;

@Service
@Slf4j
public class OrderRecordHandlerImpl implements OrderRecordHandler {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventRepository orderEventRepository;

    @Override
    public CompletableFuture<Void> onEvent(OrderEvent event) {
        return CompletableFuture.runAsync(() -> {
            switch (event.eventName()) {
                case CREATE:
                    createOrder(event);
                    break;
                case PREPARE:
                    modyfiOrder(event);
                default:
                    throw new RuntimeException("unsurpported event name");
            }
        });
    }


    private void createOrder(OrderEvent event) {
        log.info("Create order id = {}", event.id());
        OrderRecord entity = OrderRecord.builder()
                .orderId(event.id())
                .status(CREATED).build();
        OrderRecord result = orderRepository.save(entity);
        log.info("saved order = {}", result);
    }

    private void modyfiOrder(OrderEvent event) {
        orderRepository.findById(event.id()).ifPresent(orderRecord -> {
            final OrderStatus status = statusMachineMap(orderRecord, event);
            orderRecord.setStatus(status);
            orderRepository.save(orderRecord);
        });
    }

    private OrderStatus statusMachineMap(OrderRecord orderRecord, OrderEvent event) {
        switch (event.eventName()) {
            case CREATE:
                return CREATED;
            case PREPARE:
                return PREPARING;
            default:
                throw new RuntimeException("unsurpported event name");
        }
    }
}
