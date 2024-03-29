package org.example.event.sourcing.order.poc.query.order.domain.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.modules.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.modules.observation.annotation.LogInfo;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderEventRecord;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderStatus;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderEventRecordHandler;
import org.example.event.sourcing.order.poc.query.order.domain.repo.OrderEventRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@LogInfo
public class OrderEventRecordHandlerImpl implements OrderEventRecordHandler {

    private final OrderEventRepository orderEventRepository;

    @Override
    public void onEvent(OrderEvent event) {
        switch (event.eventName()) {
            case CREATED:
                createOrder(event);
                break;
            case COMPLETED:
                prepareOrder(event);
                break;
            default:
                throw new RuntimeException("unsupported event name");
        }
    }


    private void createOrder(OrderEvent event) {
        log.info("Create order id = {}", event.id());
        OrderEventRecord entity = OrderEventRecord.builder()
                .orderId(event.id())
                .toStatus(OrderStatus.CREATED)
                .eventName(event.eventName())
                .build();
        OrderEventRecord result = orderEventRepository.save(entity);
        log.info("saved order event = {}", result);
    }

    private void prepareOrder(OrderEvent event) {
        log.info("Prepare order id = {}", event.id());
        OrderEventRecord entity = OrderEventRecord.builder()
                .orderId(event.id())
                .fromStatus(OrderStatus.CREATED)
                .toStatus(OrderStatus.PREPARING)
                .eventName(event.eventName())
                .build();
        OrderEventRecord result = orderEventRepository.save(entity);
        log.info("saved order event = {}", result);
    }

}
