package org.example.event.sourcing.order.poc.common.model.event;

public enum OrderEventName {
    CREATE, PREPARE, LOGISTICS, PAYED, RECEIVED, FINISH;
}
