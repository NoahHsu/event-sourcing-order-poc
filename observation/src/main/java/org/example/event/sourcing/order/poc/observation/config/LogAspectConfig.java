package org.example.event.sourcing.order.poc.observation.config;

import org.example.event.sourcing.order.poc.observation.aop.AbstractLogAspect;
import org.example.event.sourcing.order.poc.observation.aop.DefaultLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogAspectConfig {

    @Bean
    @ConditionalOnMissingBean
    AbstractLogAspect defaultLogAspect() {
        return new DefaultLogAspect();
    }

}
