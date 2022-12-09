package org.example.event.sourcing.order.poc.handler.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EventHandlerOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventHandlerOrderApplication.class, args);
    }

}
