package org.example.event.sourcing.order.poc.modules.client.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.event.sourcing.order.poc.modules.client.order.config.OrderCommandClientConfig;
import org.example.event.sourcing.order.poc.common.model.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OrderCommandClientConfig.class, OrderMockCommandServerConfig.class})
class OrderCommandClientTest {

    @Autowired
    @Qualifier("mockOrderCommandServer")
    private WireMockServer mockOrderCommandServer;

    @Autowired
    private OrderCommandClient orderCommandClient;

    @Test
    void givenOkResponse_whenCreateOrder_thenOrderShouldBeReturn() {
        final Order givenInput = new Order("22222");
        final String givenResponseBody = """
                {
                    "id": "22222"
                }""";
        final String givenUrl = "/api/v1/orders";
        mockOrderCommandServer.stubFor(WireMock.post(WireMock.urlEqualTo(givenUrl))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(givenResponseBody)));

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
        final String givenResponseBody = """
                {
                    "result": "OK"
                }""";
        final String givenUrl = "/api/v1/orders/complete";
        mockOrderCommandServer.stubFor(WireMock.post(WireMock.urlEqualTo(givenUrl))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(givenResponseBody)));

        final Map<String, String> actualOutput = orderCommandClient.complete(givenInput);

        final Map<String, String> expectedOutput = Map.of(responseKey, "OK");
        then(actualOutput.get(responseKey))
                .as("Check that Order ID is the same as input.")
                .isNotEmpty()
                .isEqualTo(expectedOutput.get(responseKey));

    }

}
