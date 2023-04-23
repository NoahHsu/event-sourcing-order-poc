package org.example.event.sourcing.order.poc.handler.payment.domain;

import org.example.event.sourcing.order.poc.event.model.PaymentEvent;

import java.util.concurrent.CompletableFuture;

public interface PaymentEventHandler {

    CompletableFuture<Void> onEvent(PaymentEvent event);

}
