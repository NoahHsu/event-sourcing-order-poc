package org.example.event.sourcing.order.poc.client.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;

public class OrderCommandMockServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockOrderCommandServer() {
        return new WireMockServer(8081);
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
