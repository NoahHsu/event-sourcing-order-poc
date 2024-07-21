package org.example.event.sourcing.order.poc.modules.client.order;

import org.example.event.sourcing.order.poc.modules.client.order.config.OrderQueryClientConfig;
import org.example.event.sourcing.order.poc.modules.client.order.exception.ResourceNotFoundException;
import org.example.event.sourcing.order.poc.modules.client.order.model.V1Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.time.Instant;

import static org.assertj.core.api.BDDAssertions.catchRuntimeException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.example.event.sourcing.order.poc.modules.client.order.model.V1OrderStatus.CREATED;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OrderQueryClientConfig.class, ObserveMockConfig.class})
@Testcontainers
class OrderQueryClientTest {

    @Container
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0")
            .withMappingFromResource(OrderQueryClientTest.class, "order-query.json");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("cluster.order.query.base-url", wiremockServer::getBaseUrl);
    }

    @Autowired
    private OrderQueryClient orderQueryClient;

    @Test
    void givenOkResponse_whenGetOrder_thenOrderShouldBeReturn() {
        final String givenId = "1111";

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
        final String givenId = "2222";

        RuntimeException actualException = catchRuntimeException(
                () -> orderQueryClient.get(givenId));
        then(actualException)
                .isInstanceOf(ResourceNotFoundException.class);
    }

}
