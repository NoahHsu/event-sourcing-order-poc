package org.example.event.sourcing.order.poc.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommandSideApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommandSideApplication.class, args);
    }
}
