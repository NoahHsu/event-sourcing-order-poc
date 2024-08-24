package org.example.event.sourcing.order.poc.modules.client.order;

import org.example.event.sourcing.order.poc.modules.client.order.config.OrderQueryClientConfig;
import org.example.event.sourcing.order.poc.modules.client.order.exception.ResourceNotFoundException;
import org.example.event.sourcing.order.poc.modules.client.order.model.V1Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.assertj.core.api.BDDAssertions.catchRuntimeException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.example.event.sourcing.order.poc.modules.client.order.model.V1OrderStatus.CREATED;

@SpringBootTest(classes = {OrderQueryClientConfig.class, ObserveMockConfig.class})
class OrderQueryClientTest {

    @Autowired
    private OrderQueryClient orderQueryClient;

    @Test
    void givenOkResponse_whenGetOrder_thenOrderShouldBeReturn() {
        final String givenId = "22222";

        final V1Order actualOutput = orderQueryClient.get(givenId);

        final V1Order expectedOutput = V1Order.builder()
                .status(CREATED)
                .createdDate(Instant.parse("2023-04-01T12:44:41.658253Z"))
                .updatedDate(Instant.parse("2023-04-01T12:44:41.658253Z"))
                .build();
        then(actualOutput)
                .as("Check that returned V1Order is deserialized successfully.")
                .isEqualTo(expectedOutput);
    }

    @Test
    void givenNotFoundResponse_whenGetOrder_thenExceptionShouldBeThrown() {
        final String givenId = "404";

        RuntimeException actualException = catchRuntimeException(
                () -> orderQueryClient.get(givenId));
        then(actualException)
                .isInstanceOf(ResourceNotFoundException.class);
    }

}
