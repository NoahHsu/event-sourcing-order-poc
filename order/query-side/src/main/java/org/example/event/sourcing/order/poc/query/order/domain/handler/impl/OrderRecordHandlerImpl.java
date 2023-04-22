package org.example.event.sourcing.order.poc.query.order.domain.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.example.event.sourcing.order.poc.observation.annotation.LogInfo;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderRecord;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderStatus;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderRecordHandler;
import org.example.event.sourcing.order.poc.query.order.domain.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static org.example.event.sourcing.order.poc.query.order.domain.entity.OrderStatus.CREATED;
import static org.example.event.sourcing.order.poc.query.order.domain.entity.OrderStatus.FINISHED;

@Service
@RequiredArgsConstructor
@Slf4j
@LogInfo
public class OrderRecordHandlerImpl implements OrderRecordHandler {

    private final OrderRepository orderRepository;

    @Override
    public CompletableFuture<Void> onEvent(OrderEvent event) {
        return CompletableFuture.runAsync(() -> {
            switch (event.eventName()) {
                case CREATED:
                    createOrder(event);
                    break;
                case COMPLETED:
                    completeOrder(event);
                    break;
                default:
                    throw new RuntimeException("unsupported event name");
            }
        });
    }


    private void createOrder(OrderEvent event) {
        log.info("Create order id = {}", event.id());
        OrderRecord entity = OrderRecord.builder()
                .orderId(event.id())
                .status(CREATED)
                .createdDate(event.createdDate())
                .updatedDate(event.createdDate())
                .build();
        OrderRecord result = orderRepository.save(entity);
        log.info("saved order = {}", result);
    }

    private void completeOrder(OrderEvent event) {
        orderRepository.findById(event.id()).ifPresent(orderRecord -> {
            final OrderStatus status = statusMachineMap(orderRecord, event);
            orderRecord.setStatus(status);
            orderRecord.setUpdatedDate(event.createdDate());
            orderRepository.save(orderRecord);
        });
    }

    private OrderStatus statusMachineMap(OrderRecord orderRecord, OrderEvent event) {
        return switch (event.eventName()) {
            case CREATED -> CREATED;
            case COMPLETED -> FINISHED;
            default -> throw new RuntimeException("unsupported event name");
        };
    }
}
