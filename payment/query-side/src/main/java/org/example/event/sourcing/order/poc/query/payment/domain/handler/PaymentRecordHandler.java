package org.example.event.sourcing.order.poc.query.payment.domain.handler;

import org.example.event.sourcing.order.poc.event.model.PaymentEvent;

import java.util.concurrent.CompletableFuture;

public interface PaymentRecordHandler {

    CompletableFuture<Void> onEvent(PaymentEvent event);

}
