package org.example.event.sourcing.order.poc.modules.observation.config;

import org.example.event.sourcing.order.poc.modules.observation.aop.AbstractLogAspect;
import org.example.event.sourcing.order.poc.modules.observation.aop.DefaultLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class LogAspectConfig {

    @Bean
    @ConditionalOnMissingBean
    AbstractLogAspect defaultLogAspect() {
        return new DefaultLogAspect();
    }

}
