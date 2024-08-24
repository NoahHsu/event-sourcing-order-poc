package org.example.event.sourcing.order.poc.modules.client.order;

import org.example.event.sourcing.order.poc.modules.client.order.config.OrderCommandClientConfig;
import org.example.event.sourcing.order.poc.modules.common.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = {OrderCommandClientConfig.class, ObserveMockConfig.class})
@TestPropertySource(properties = "spring.config.additional-location=file:../../config/")
class OrderCommandClientTest {

    @Autowired
    private OrderCommandClient orderCommandClient;

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
