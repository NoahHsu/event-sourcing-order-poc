package org.example.event.sourcing.order.poc.query.order.consumer;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.modules.event.model.OrderEvent;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderEventRecordHandler;
import org.example.event.sourcing.order.poc.query.order.domain.handler.OrderRecordHandler;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.invocation.MethodArgumentResolutionException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.SocketException;

import static org.example.event.sourcing.order.poc.modules.event.model.OrderEvent.ORDER_STATUS_GROUP_ID_PREFIX;
import static org.example.event.sourcing.order.poc.modules.event.model.OrderEvent.ORDER_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
@Observed
public class OrderEventConsumer {

    private final OrderRecordHandler orderRecordHandler;

    private final OrderEventRecordHandler orderEventRecordHandler;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @RetryableTopic(kafkaTemplate = "kafkaTemplate",
            exclude = {DeserializationException.class,
                    MessageConversionException.class,
                    ConversionException.class,
                    MethodArgumentResolutionException.class,
                    NoSuchMethodException.class,
                    ClassCastException.class},
            attempts = "4",
            backoff = @Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000)
    )
    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_STATUS_GROUP_ID_PREFIX + "#{ T(java.util.UUID).randomUUID().toString() }")
    @Transactional
    public void orderEventListener(@Header(KafkaHeaders.RECEIVED_TOPIC) String receivedTopic,
                                   OrderEvent orderEvent, Acknowledgment ack) throws SocketException {
        log.info("Topic({}) handler receive data = {}", receivedTopic, orderEvent);
        try {
            orderEventRecordHandler.onEvent(orderEvent);
            if (receivedTopic.contains("retry")) {
                orderRecordHandler.onRequeueEvent(orderEvent);
            } else {
                orderRecordHandler.onEvent(orderEvent);
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.warn("Fail to handle event {}.", orderEvent);
            throw e;
        }
    }

    @DltHandler
    public void processMessage(OrderEvent message) {
        log.error("{} DltHandler processMessage = {}", ORDER_TOPIC, message);
    }

}