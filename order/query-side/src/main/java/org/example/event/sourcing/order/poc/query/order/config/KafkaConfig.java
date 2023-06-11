package org.example.event.sourcing.order.poc.query.order.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.backoff.FixedBackOff;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class KafkaConfig extends RetryTopicConfigurationSupport {

    @Override
    protected void configureBlockingRetries(BlockingRetriesConfigurer blockingRetries) {
        blockingRetries
                .retryOn(IOException.class)
                .backOff(new FixedBackOff(5000, 3));
    }

}
