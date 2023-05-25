package org.example.event.sourcing.order.poc.client.order.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.example.event.sourcing.order.poc.client.order.OrderQueryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import java.util.List;

@AutoConfiguration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class OrderQueryClientConfig {

    @Value("${cluster.order.query.base-url:http://localhost:8083}")
    private String url;

    @Bean
    @Observed
    public OrderQueryClient orderQueryClient(ObservationRegistry observationRegistry, MeterRegistry meterRegistry) {
        Feign.Builder builder = Feign.builder()
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .encoder(new JacksonEncoder(List.of(new JavaTimeModule())))
                .decoder(new JacksonDecoder(List.of(new JavaTimeModule())))
                .addCapability(new MicrometerObservationCapability(observationRegistry))
                .addCapability(new MicrometerCapability(meterRegistry));
        return new OrderQueryClient(builder, url);
    }

}
