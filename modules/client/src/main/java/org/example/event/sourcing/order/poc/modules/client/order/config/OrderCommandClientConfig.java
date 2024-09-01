package org.example.event.sourcing.order.poc.modules.client.order.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.micrometer.MicrometerCapability;
import feign.micrometer.MicrometerObservationCapability;
import feign.slf4j.Slf4jLogger;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import org.example.event.sourcing.order.poc.modules.client.order.OrderCommandClient;
import org.example.event.sourcing.order.poc.modules.client.order.decoder.CustomErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static org.example.event.sourcing.order.poc.modules.client.order.model.ResourceName.ORDER;

@AutoConfiguration
public class OrderCommandClientConfig {

    @Value("${cluster.order.command.base-url}")
    private String url;

    @Bean
    @Observed
    public OrderCommandClient orderCommandClient(ObservationRegistry observationRegistry, MeterRegistry meterRegistry) {
        return Feign.builder()
                .errorDecoder(new CustomErrorDecoder(ORDER))
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .encoder(new JacksonEncoder(List.of(new JavaTimeModule())))
                .decoder(new JacksonDecoder(List.of(new JavaTimeModule())))
                .options(new Request.Options(3000, 5000))
                .addCapability(new MicrometerObservationCapability(observationRegistry))
                .addCapability(new MicrometerCapability(meterRegistry))
                .target(OrderCommandClient.class, url);
    }

}
