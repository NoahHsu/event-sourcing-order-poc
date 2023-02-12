package org.example.event.sourcing.order.poc.client.order.config;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.client.order.OrderCommandClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OrderCommandClientConfig {

    @Bean
    public OrderCommandClient orderCommandClient() {
        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OrderCommandClient.class, "http://localhost:8081");
    }
}
