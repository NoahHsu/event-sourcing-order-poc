package org.example.event.sourcing.order.poc.query.payment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.backoff.FixedBackOff;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class KafkaConfig extends RetryTopicConfigurationSupport {

    @Override
    protected void configureBlockingRetries(RetryTopicConfigurationSupport.BlockingRetriesConfigurer blockingRetries) {
        blockingRetries
                .retryOn(IOException.class)
                .backOff(new FixedBackOff(5000, 3));
    }

}
