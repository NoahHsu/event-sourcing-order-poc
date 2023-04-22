package org.example.event.sourcing.order.poc.client.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ObserveMockConfig.class)
public class OrderMockQueryServerConfig {

    @Bean(name = "mockOrderQueryServer", initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockOrderQueryServer() {
        return new WireMockServer(8083);
    }

}
