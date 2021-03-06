package org.example.event.sourcing.order.poc.query.domain.handler.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.example.event.sourcing.order.poc.query.domain.entity.OrderEventRecord;
import org.example.event.sourcing.order.poc.query.domain.handler.OrderEventRecordHandler;
import org.example.event.sourcing.order.poc.query.domain.repo.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static org.example.event.sourcing.order.poc.query.domain.entity.OrderStatus.CREATED;
import static org.example.event.sourcing.order.poc.query.domain.entity.OrderStatus.PREPARING;

@Service
@Slf4j
public class OrderEventRecordHandlerImpl implements OrderEventRecordHandler {

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
                    prepareOrder(event);
                default:
                    throw new RuntimeException("unsurpported event name");
            }
        });
    }


    private void createOrder(OrderEvent event) {
        log.info("Create order id = {}", event.id());
        OrderEventRecord entity = OrderEventRecord.builder()
                .orderId(event.id())
                .toStatus(CREATED)
                .eventName(event.eventName())
                .build();
        OrderEventRecord result = orderEventRepository.save(entity);
        log.info("saved order event = {}", result);
    }

    private void prepareOrder(OrderEvent event) {
        log.info("Prepare order id = {}", event.id());
        OrderEventRecord entity = OrderEventRecord.builder()
                .orderId(event.id())
                .fromStatus(CREATED)
                .toStatus(PREPARING)
                .eventName(event.eventName())
                .build();
        OrderEventRecord result = orderEventRepository.save(entity);
        log.info("saved order event = {}", result);
    }

}
