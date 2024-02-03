package org.example.event.sourcing.order.poc.command.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.modules.client.order.OrderQueryClient;
import org.example.event.sourcing.order.poc.modules.client.order.exception.ResourceNotFoundException;
import org.example.event.sourcing.order.poc.modules.client.order.model.V1Order;
import org.example.event.sourcing.order.poc.modules.client.order.model.V1OrderStatus;
import org.example.event.sourcing.order.poc.command.order.producer.OrderEventProducer;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.example.event.sourcing.order.poc.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.modules.observation.annotation.LogInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import static org.example.event.sourcing.order.poc.event.model.OrderEventName.COMPLETED;
import static org.example.event.sourcing.order.poc.event.model.OrderEventName.CREATED;

@Service
@RequiredArgsConstructor
@Slf4j
@LogInfo
public class OrderService {

    private final OrderQueryClient orderQueryClient;
    private final OrderEventProducer orderEventProducer;

    private Random rand = new Random();  // SecureRandom is preferred to Random

    public Order createOrder(Order order) {
        Optional<V1Order> queryResult = queryOrder(order.id());
        if (queryResult.isPresent()) {
            return order;
        } else {
            simulateComplexity();
            boolean isSuccess = orderEventProducer.create(new OrderEvent(order.id(), CREATED, Instant.now()));
            if (isSuccess) {
                return order;
            } else {
                log.warn("create Order event fail", order);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "send event fail.");
            }
        }
    }

    private Optional<V1Order> queryOrder(String id) {
        try {
            return Optional.of(orderQueryClient.get(id));
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }

    private void simulateComplexity() {
        try {
            int sleepTime = rand.nextInt(2500) + 500;
            log.info("sleep for {} millis to simulate the complex execution.", sleepTime);
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            log.warn("callVendorApi fail");
            Thread.currentThread().interrupt();
        }
    }

    public String completeOrder(String id) {
        V1Order v1order = queryOrder(id)
                .orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND));
        if (v1order.status() == V1OrderStatus.CREATED) {
            orderEventProducer.create(new OrderEvent(id, COMPLETED, Instant.now()));
            return "OK";
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "order(id = {}) is not in right status.");
        }
    }

}
