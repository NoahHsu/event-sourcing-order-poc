package org.example.event.sourcing.order.poc.client.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.event.sourcing.order.poc.client.order.config.OrderQueryClientConfig;
import org.example.event.sourcing.order.poc.client.order.model.V1Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.assertj.core.api.BDDAssertions.then;
import static org.example.event.sourcing.order.poc.client.order.model.V1OrderStatus.CREATED;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OrderQueryClientConfig.class, OrderMockQueryServerConfig.class})
class OrderQueryClientTest {

    @Autowired
    @Qualifier("mockOrderQueryServer")
    private WireMockServer mockOrderQueryServer;

    @Autowired
    private OrderQueryClient orderQueryClient;

    @Test
    void whenGetOrderById_thenOrderShouldBeReturn() {
        final String givenId = "1111";
        final String givenUrl = "/api/v1-orders/1111";
        final String givenResponseBody = """
                {
                    "status": "CREATED",
                    "createdDate": "2023-04-01T12:44:41.658253Z",
                    "updatedDate": "2023-04-01T12:44:41.658253Z",
                    "_links": {
                      "self": {
                        "href": "http://localhost:8083/api/v1-orders/1111"
                      },
                      "orderRecord": {
                        "href": "http://localhost:8083/api/v1-orders/1111"
                      }
                    }
                  }""";

        mockOrderQueryServer.stubFor(WireMock.get(WireMock.urlEqualTo(givenUrl))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", HAL_JSON_VALUE)
                        .withBody(givenResponseBody)));

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

}
