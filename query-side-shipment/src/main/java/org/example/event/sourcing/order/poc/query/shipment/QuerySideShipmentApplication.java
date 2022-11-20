package org.example.event.sourcing.order.poc.query.shipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuerySideShipmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuerySideShipmentApplication.class, args);
    }

}
