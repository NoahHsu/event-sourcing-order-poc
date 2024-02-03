package org.example.event.sourcing.order.poc.query.order.domain.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.modules.observation.annotation.LogInfo;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderRecord;
import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderStatus;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderRecordHandler;
import org.example.event.sourcing.order.poc.query.order.domain.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.net.SocketException;
import java.security.SecureRandom;
import java.util.Random;

import static org.example.event.sourcing.order.poc.query.order.domain.entity.OrderStatus.CREATED;
import static org.example.event.sourcing.order.poc.query.order.domain.entity.OrderStatus.FINISHED;

@Service
@RequiredArgsConstructor
@Slf4j
@LogInfo
public class OrderRecordHandlerImpl implements OrderRecordHandler {

    private final OrderRepository orderRepository;

    @Override
    public void onEvent(OrderEvent event) throws SocketException {
        switch (event.id().charAt(0)) {
            case 'n':
                throw new SocketException();
            case 'd':
                throw new ClassCastException();
            case 'e':
                throw new RuntimeException();
            default:
                onNormalEvent(event);
        }
    }

    @Override
    public void onRequeueEvent(OrderEvent event) {
        switch (event.id().charAt(0)) {
            case 'd':
                throw new ClassCastException();
            case 'e':
                Random random = new SecureRandom();
                if (random.nextBoolean()) {
                    throw new RuntimeException();
                } else {
                    onNormalEvent(event);
                    return;
                }
            default:
                onNormalEvent(event);
        }
    }

    private void onNormalEvent(OrderEvent event) {
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
    }


    private void createOrder(OrderEvent event) {
        String orderId = event.id();
        log.info("Create order id = {}", orderId);
        boolean isExist = orderRepository.existsById(orderId);
        if (!isExist) {
            OrderRecord entity = OrderRecord.builder()
                    .orderId(orderId)
                    .status(CREATED)
                    .createdDate(event.createdDate())
                    .updatedDate(event.createdDate())
                    .build();
            OrderRecord result = orderRepository.save(entity);
            log.info("saved order = {}", result);
        } else {
            log.warn("order id = {} had been created.", orderId);
        }
    }

    private void completeOrder(OrderEvent event) {
        orderRepository.findById(event.id()).ifPresent(orderRecord -> {
            if (orderRecord.getStatus() == CREATED) {
                final OrderStatus status = statusMachineMap(orderRecord, event);
                orderRecord.setStatus(status);
                orderRecord.setUpdatedDate(event.createdDate());
                orderRepository.save(orderRecord);
            }
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
