package org.example.event.sourcing.order.poc.modules.client.order.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
    
}
