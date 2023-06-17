package org.example.event.sourcing.order.poc.query.order.domain.handler;

import org.example.event.sourcing.order.poc.event.model.OrderEvent;

import java.net.SocketException;

public interface OrderRecordHandler {
    void onEvent(OrderEvent event) throws SocketException;

    void onRequeueEvent(OrderEvent event);

}
