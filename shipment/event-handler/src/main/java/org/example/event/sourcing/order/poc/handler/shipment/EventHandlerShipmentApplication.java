package org.example.event.sourcing.order.poc.handler.shipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
        "org.example.event.sourcing.order.poc.handler.shipment",
        "org.example.event.sourcing.order.poc.client"})
@EnableJpaAuditing
public class EventHandlerShipmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventHandlerShipmentApplication.class, args);
    }

}
