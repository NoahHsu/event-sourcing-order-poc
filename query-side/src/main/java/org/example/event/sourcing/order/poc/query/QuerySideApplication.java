package org.example.event.sourcing.order.poc.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QuerySideApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuerySideApplication.class, args);
    }
}
