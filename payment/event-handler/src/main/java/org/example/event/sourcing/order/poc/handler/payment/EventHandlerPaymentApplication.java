package org.example.event.sourcing.order.poc.handler.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
        "org.example.event.sourcing.order.poc.handler.payment",
        "org.example.event.sourcing.order.poc.client"})
@EnableJpaAuditing
public class EventHandlerPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventHandlerPaymentApplication.class, args);
    }

}
