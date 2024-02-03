package org.example.event.sourcing.order.poc.query.order.domain.handler;

import org.example.event.sourcing.order.poc.modules.event.model.OrderEvent;

public interface OrderEventRecordHandler {
    void onEvent(OrderEvent event);
    
}
