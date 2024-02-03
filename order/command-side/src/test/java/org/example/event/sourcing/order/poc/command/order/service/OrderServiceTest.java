package org.example.event.sourcing.order.poc.command.order.service;

import org.example.event.sourcing.order.poc.modules.client.order.OrderQueryClient;
import org.example.event.sourcing.order.poc.modules.client.order.exception.ResourceNotFoundException;
import org.example.event.sourcing.order.poc.modules.client.order.model.V1Order;
import org.example.event.sourcing.order.poc.modules.client.order.model.V1OrderStatus;
import org.example.event.sourcing.order.poc.command.order.producer.OrderEventProducer;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.example.event.sourcing.order.poc.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.event.model.OrderEventName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.BDDMockito;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchRuntimeException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.example.event.sourcing.order.poc.event.model.OrderEventName.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

class OrderServiceTest {

    private OrderQueryClient orderQueryClient = mock(OrderQueryClient.class);

    private OrderEventProducer orderEventProducer = mock(OrderEventProducer.class);

    private OrderService orderService = new OrderService(orderQueryClient, orderEventProducer);


    @Test
    void givenIdOfCreatedOrder_whenCreateOrder_thenShouldReturnCreatedOrderAndNotProduceEvent() {
        String givenId = "1111";
        given(orderQueryClient.get(givenId))
                .willReturn(givenV1Order(V1OrderStatus.CREATED));

        Order actualReturn = orderService.createOrder(new Order(givenId));

        BDDMockito.then(orderEventProducer).shouldHaveNoInteractions();
        then(actualReturn).extracting(Order::id).isEqualTo(givenId);
    }

    @Test
    void givenIdOfNotCreatedOrder_whenCreateOrder_thenShouldReturnCreatedOrderAndProduceEvent() {
        String givenId = "1111";
        given(orderQueryClient.get(givenId))
                .willThrow(ResourceNotFoundException.class);
        given(orderEventProducer.create(any())).willReturn(Boolean.TRUE);

        Order actualReturn = orderService.createOrder(new Order(givenId));

        BDDMockito.then(orderEventProducer).should()
                .create(assertArg(oe -> {
                    assertEquals(givenId, oe.id());
                    assertEquals(CREATED, oe.eventName());
                }));
        then(actualReturn).extracting(Order::id).isEqualTo(givenId);
    }

    @Test
    void givenIdOfCreatedOrder_whenCompleteOrder_thenShouldSuccess() {
        String givenId = "1111";
        given(orderQueryClient.get(givenId))
                .willReturn(givenV1Order(V1OrderStatus.CREATED));

        String actualReturn = orderService.completeOrder(givenId);

        BDDMockito.then(orderEventProducer).should()
                .create(argThat(OrderEventArgumentMatcher(givenId)));
        then(actualReturn).isEqualTo("OK");
    }

    private V1Order givenV1Order(V1OrderStatus status) {
        return V1Order.builder()
                .status(status)
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();
    }

    private static ArgumentMatcher<OrderEvent> OrderEventArgumentMatcher(String givenId) {
        return actual -> actual.id().equals(givenId) &&
                actual.eventName() == OrderEventName.COMPLETED;
    }

    @Test
    void givenIdOfInLogisticOrder_whenCompleteOrder_then422ErrorShouldBeThrown() {
        String givenId = "1111";
        given(orderQueryClient.get(givenId))
                .willReturn(givenV1Order(V1OrderStatus.IN_LOGISTICS));

        RuntimeException actualException = catchRuntimeException(
                () -> orderService.completeOrder(givenId));
        then(actualException)
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("422");
        BDDMockito.then(orderEventProducer).shouldHaveNoInteractions();
    }

    @Test
    void givenIdOfNotExistOrder_whenCompleteOrder_then404ErrorShouldBeThrown() {
        String givenId = "1111";
        given(orderQueryClient.get(givenId))
                .willThrow(ResourceNotFoundException.class);

        RuntimeException actualException = catchRuntimeException(
                () -> orderService.completeOrder(givenId));
        then(actualException)
                .isInstanceOf(ErrorResponseException.class)
                .hasMessageContaining("404");
        BDDMockito.then(orderEventProducer).shouldHaveNoInteractions();
    }

}
