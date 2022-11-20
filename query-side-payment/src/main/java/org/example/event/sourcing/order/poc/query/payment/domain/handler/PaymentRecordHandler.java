package org.example.event.sourcing.order.poc.query.payment.domain.handler;

import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;

import java.util.concurrent.CompletableFuture;

public interface PaymentRecordHandler {

    CompletableFuture<Void> onEvent(PaymentEvent event);

}
