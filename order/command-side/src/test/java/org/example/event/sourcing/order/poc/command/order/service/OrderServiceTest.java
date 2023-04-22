package org.example.event.sourcing.order.poc.command.order.service;

import org.example.event.sourcing.order.poc.client.order.OrderQueryClient;
import org.example.event.sourcing.order.poc.client.order.model.V1Order;
import org.example.event.sourcing.order.poc.client.order.model.V1OrderStatus;
import org.example.event.sourcing.order.poc.command.order.producer.OrderEventProducer;
import org.example.event.sourcing.order.poc.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.event.model.OrderEventName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.BDDMockito;

import java.time.Instant;

import static org.assertj.core.api.BDDAssertions.catchRuntimeException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

class OrderServiceTest {

    private OrderQueryClient orderQueryClient = mock(OrderQueryClient.class);

    private OrderEventProducer orderEventProducer = mock(OrderEventProducer.class);

    private OrderService orderService = new OrderService(orderQueryClient, orderEventProducer);

    @Test
    void shouldComplete() {
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
    void shouldFailComplete() {
        String givenId = "1111";
        given(orderQueryClient.get(givenId))
                .willReturn(givenV1Order(V1OrderStatus.IN_LOGISTICS));

        RuntimeException actualException = catchRuntimeException(
                () -> orderService.completeOrder(givenId));
        then(actualException)
                .hasMessage("order(id = {}) is not in right status.", givenId);
        BDDMockito.then(orderEventProducer).shouldHaveNoInteractions();
    }

}
