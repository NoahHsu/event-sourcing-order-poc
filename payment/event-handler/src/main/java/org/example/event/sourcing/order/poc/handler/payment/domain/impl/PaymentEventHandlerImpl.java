package org.example.event.sourcing.order.poc.handler.payment.domain.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;
import org.example.event.sourcing.order.poc.handler.payment.domain.PaymentEventHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventHandlerImpl implements PaymentEventHandler {

    @Override
    public CompletableFuture<Void> onEvent(PaymentEvent event) {
        return CompletableFuture.runAsync(() -> {
            switch (event.eventName()) {
                case CREATED:
                    handlePaymentEvent(event);
                    break;
                default:
                    throw new RuntimeException("unsupported event");
            }
        });
    }

    private void handlePaymentEvent(PaymentEvent event) {
        log.info(event.toString());
    }

}
