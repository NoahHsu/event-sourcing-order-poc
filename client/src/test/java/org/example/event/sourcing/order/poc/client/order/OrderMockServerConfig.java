package org.example.event.sourcing.order.poc.client.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;

public class OrderMockServerConfig {

    @Bean(name = "mockOrderCommandServer", initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockOrderCommandServer() {
        return new WireMockServer(8081);
    }

    @Bean(name = "mockOrderQueryServer", initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockOrderQueryServer() {
        return new WireMockServer(8083);
    }

    @Bean
    ObservationRegistry observationRegistry() {
        return ObservationRegistry.create();
    }

    @Bean
    MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

}
