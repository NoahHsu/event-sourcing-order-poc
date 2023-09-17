package org.example.event.sourcing.order.poc.external.client.toggle.flagr.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Logger;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.example.event.sourcing.order.poc.external.client.toggle.flagr.FlagrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class FlagrClientConfig {

    @Value("${external.flagr.base-url:http://localhost:18000}")
    private String url;

    @Bean
    public FlagrClient flagrClient() {
        return Feign.builder()
                .errorDecoder(new ErrorDecoder.Default())
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .encoder(new JacksonEncoder(List.of(new JavaTimeModule())))
                .decoder(new JacksonDecoder(List.of(new JavaTimeModule())))
                .target(FlagrClient.class, url);
    }

}
