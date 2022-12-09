package org.example.event.sourcing.order.poc.command.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommandSidePaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommandSidePaymentApplication.class, args);
    }
}
