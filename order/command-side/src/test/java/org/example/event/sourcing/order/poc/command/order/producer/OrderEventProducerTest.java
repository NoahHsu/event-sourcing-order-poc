package org.example.event.sourcing.order.poc.command.order.producer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.assertj.core.api.BDDAssertions;
import org.example.event.sourcing.order.poc.modules.event.model.OrderEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static org.example.event.sourcing.order.poc.modules.event.model.OrderEvent.ORDER_TOPIC;
import static org.example.event.sourcing.order.poc.modules.event.model.OrderEventName.CREATED;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class OrderEventProducerTest {

    private static ListAppender<ILoggingEvent> listAppender;

    @SuppressWarnings("unchecked")
    private KafkaTemplate<String, OrderEvent> kafkaTemplate = mock(KafkaTemplate.class);

    private OrderEventProducer orderEventProducer = new OrderEventProducer(kafkaTemplate);

    private String orderId = "id0";
    private String topic = "topicX";
    private int partition = 1;
    private long timestamp = System.currentTimeMillis();

    private OrderEvent order;
    private SendResult<String, OrderEvent> sendResult;

    @BeforeAll
    static void setupLogger() {
        listAppender = new ListAppender<>();
        listAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        ((Logger) LoggerFactory.getLogger(OrderEventProducer.class)).addAppender(listAppender);
        listAppender.start();
    }

    @BeforeEach
    void setup() {
        order = new OrderEvent(orderId, CREATED, Instant.now());
        sendResult = buildSendResult();
        listAppender.list.clear();
    }

    @AfterAll
    static void tearDown() {
        listAppender.stop();
        ((Logger) LoggerFactory.getLogger(OrderEventProducer.class)).detachAppender(listAppender);

    }

    private SendResult<String, OrderEvent> buildSendResult() {
        ProducerRecord<String, OrderEvent> producerRecord = new ProducerRecord<>(topic, "key", order);
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(topic, partition), 0, 0, timestamp, 0, 0);
        return new SendResult<>(producerRecord, recordMetadata);
    }

    @Test
    void givenSuccessResponse_whenCreateEvent_thenShouldSend() {
        given(kafkaTemplate.send(ORDER_TOPIC, orderId, order))
                .willReturn(CompletableFuture.completedFuture(sendResult));

        orderEventProducer.create(order);

        // verify that kafkaTemplate.send is called
        then(kafkaTemplate).should().send(ORDER_TOPIC, orderId, order);
    }

    @Test
    void givenSuccessResponse_whenCreateEvent_thenShouldLogResult() {
        given(kafkaTemplate.send(ORDER_TOPIC, orderId, order))
                .willReturn(CompletableFuture.completedFuture(sendResult));

        orderEventProducer.create(order);

        // verify that sendResult is logged
        BDDAssertions.then(listAppender.list).filteredOn(log -> Level.INFO == log.getLevel())
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(s -> s.contains("topic-partition " + topic + "-" + partition));

    }

    @Test
    void givenSuccessResponse_whenCreateEvent_thenShouldLogFailReason() {
        given(kafkaTemplate.send(ORDER_TOPIC, orderId, order))
                .willReturn(CompletableFuture.failedFuture(new RuntimeException("Deserved it")));

        orderEventProducer.create(order);

        // verify that send failure is logged
        BDDAssertions.then(listAppender.list).filteredOn(log -> Level.WARN == log.getLevel())
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(s -> s.contains("Unable to write Order to topic"));
    }

}
