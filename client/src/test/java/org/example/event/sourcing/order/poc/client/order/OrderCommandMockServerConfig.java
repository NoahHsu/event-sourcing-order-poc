package org.example.event.sourcing.order.poc.client.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.context.annotation.Bean;

public class OrderCommandMockServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockOrderCommandServer() {
        return new WireMockServer(8081);
    }
}
