package org.example.event.sourcing.order.poc.query.payment.domain.handler;

import org.example.event.sourcing.order.poc.modules.event.model.PaymentEvent;

public interface PaymentRecordHandler {

    void onEvent(PaymentEvent event);

}
