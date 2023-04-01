package org.example.event.sourcing.order.poc.common.model.event;

import java.time.Instant;

public record OrderEvent(String id, OrderEventName eventName, Instant createdDate) {

    public static final String ORDER_TOPIC = "ORDER";
    public static final int ORDER_TOPIC_PARTITION = 10;

    public static final String ORDER_EVENT_HANDLER_GROUP_ID = "ORDER-HANDLER";

    public static final String ORDER_STATUS_GROUP_ID_PREFIX = "ORDER-STATUS-";
    public static final String ORDER_LOG_GROUP_ID_PREFIX = "ORDER-LOG-";

}
