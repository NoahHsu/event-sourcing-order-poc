package org.example.event.sourcing.order.poc.handler.payment.domain;

import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;
import org.example.event.sourcing.order.poc.common.model.event.ShipmentEvent;

import java.util.concurrent.CompletableFuture;

public interface PaymentEventHandler {

    CompletableFuture<Void> onEvent(PaymentEvent event);

}
