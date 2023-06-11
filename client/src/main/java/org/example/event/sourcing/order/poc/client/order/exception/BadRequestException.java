package org.example.event.sourcing.order.poc.client.order.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
    
}
