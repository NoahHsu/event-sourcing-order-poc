package org.example.event.sourcing.order.poc.common.model.event;

public record OrderEvent(String id, String eventName) {

    public static final String ORDER_TOPIC = "ORDER";

    public static final String ORDER_STATUS_ID = "ORDER-STATUS-";
    public static final String ORDER_LOG_ID = "ORDER-LOG-";

}
