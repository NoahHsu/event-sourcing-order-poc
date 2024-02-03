package org.example.event.sourcing.order.poc.modules.client.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ObserveMockConfig.class)
public class OrderMockCommandServerConfig {

    @Bean(name = "mockOrderCommandServer", initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockOrderCommandServer() {
        return new WireMockServer(8081);
    }

}
