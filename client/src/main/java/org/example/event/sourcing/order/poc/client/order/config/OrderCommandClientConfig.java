package org.example.event.sourcing.order.poc.client.order.config;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.example.event.sourcing.order.poc.client.order.OrderCommandClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OrderCommandClientConfig {

    @Bean
    public OrderCommandClient orderCommandClient() {
        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(OrderCommandClient.class, "http://localhost:8081");
    }
}
