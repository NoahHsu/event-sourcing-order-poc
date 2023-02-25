package org.example.event.sourcing.order.poc.client.order.config;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.micrometer.MicrometerCapability;
import feign.micrometer.MicrometerObservationCapability;
import feign.slf4j.Slf4jLogger;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.example.event.sourcing.order.poc.client.order.OrderCommandClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OrderCommandClientConfig {

    @Bean
    @Observed
    public OrderCommandClient orderCommandClient(ObservationRegistry observationRegistry, MeterRegistry meterRegistry) {
        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .addCapability(new MicrometerObservationCapability(observationRegistry))
                .addCapability(new MicrometerCapability(meterRegistry))
                .target(OrderCommandClient.class, "http://localhost:8081");
    }

}
