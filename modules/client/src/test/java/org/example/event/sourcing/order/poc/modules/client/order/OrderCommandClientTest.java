package org.example.event.sourcing.order.poc.modules.client.order;

import org.example.event.sourcing.order.poc.modules.client.order.config.OrderCommandClientConfig;
import org.example.event.sourcing.order.poc.modules.common.model.Order;
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

import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OrderCommandClientConfig.class, ObserveMockConfig.class})
@Testcontainers
class OrderCommandClientTest {

    @Autowired
    private OrderCommandClient orderCommandClient;

    @Container
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0")
            .withMappingFromResource(OrderCommandClientTest.class, "order-command.json");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("cluster.order.command.base-url", wiremockServer::getBaseUrl);
    }

    @Test
    void givenOkResponse_whenCreateOrder_thenOrderShouldBeReturn() {
        final Order givenInput = new Order("22222");
        final Order actualOutput = orderCommandClient.create(givenInput);

        final Order expectedOutput = new Order("22222");
        then(actualOutput.id())
                .as("Check that Order ID is the same as input.")
                .isNotEmpty()
                .isEqualTo(expectedOutput.id());

    }

    @Test
    void givenOkResponse_whenCompleteOrder_thenOkShouldBeReturn() {
        String responseKey = "result";
        final Order givenInput = new Order("22222");
        final Map<String, String> actualOutput = orderCommandClient.complete(givenInput);

        final Map<String, String> expectedOutput = Map.of(responseKey, "OK");
        then(actualOutput.get(responseKey))
                .as("Check that Order ID is the same as input.")
                .isNotEmpty()
                .isEqualTo(expectedOutput.get(responseKey));

    }

}
