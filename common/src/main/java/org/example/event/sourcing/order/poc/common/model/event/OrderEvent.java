package org.example.event.sourcing.order.poc.common.model.event;

public record OrderEvent(String id, OrderEventName eventName) {

    public static final String ORDER_TOPIC = "ORDER";

    public static final String ORDER_STATUS_GROUP_ID_PREFIX = "ORDER-STATUS-";
    public static final String ORDER_LOG_GROUP_ID_PREFIX = "ORDER-LOG-";

}
