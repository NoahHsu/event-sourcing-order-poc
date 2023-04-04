package org.example.event.sourcing.order.poc.client.order;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;

public class ObserveMockConfig {
    @Bean
    ObservationRegistry observationRegistry() {
        return ObservationRegistry.create();
    }

    @Bean
    MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}
