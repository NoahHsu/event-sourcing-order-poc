package org.example.event.sourcing.order.poc.command.order.producer;

import static org.example.event.sourcing.order.poc.common.model.event.OrderEvent.ORDER_TOPIC;
import static org.example.event.sourcing.order.poc.common.model.event.OrderEventName.CREATED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.assertj.core.api.BDDAssertions;
import org.example.event.sourcing.order.poc.common.model.event.OrderEvent;
import org.example.event.sourcing.order.poc.common.test.InMemoryAppender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaOperations.OperationsCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

class OrderEventProducerTest {

	private static InMemoryAppender inMemoryAppender;

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
		inMemoryAppender = new InMemoryAppender();
		inMemoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		((Logger) LoggerFactory.getLogger(OrderEventProducer.class)).addAppender(inMemoryAppender);
		inMemoryAppender.start();
	}

	@BeforeEach
	void setup() {
		order = new OrderEvent(orderId, CREATED, Instant.now(), Instant.now());
		sendResult = buildSendResult();
		given(kafkaTemplate.executeInTransaction(any())).willAnswer(invocation -> {
			@SuppressWarnings("unchecked")
			OperationsCallback<String, OrderEvent, Boolean> callback = (OperationsCallback<String, OrderEvent, Boolean>) invocation
					.getArgument(0, OperationsCallback.class);
			callback.doInOperations(kafkaTemplate);
			return true;
		});
		inMemoryAppender.reset();
	}

	@AfterAll
	void tearDown() {
		inMemoryAppender.stop();
		((Logger) LoggerFactory.getLogger(OrderEventProducer.class)).detachAppender(inMemoryAppender);

	}

	private SendResult<String, OrderEvent> buildSendResult() {
		ProducerRecord<String, OrderEvent> producerRecord = new ProducerRecord<>(topic, "key", order);
		RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(topic, partition), 0, 0, timestamp, 0, 0);
		return new SendResult<>(producerRecord, recordMetadata);
	}

	@Test
	void shouldSend() {
		given(kafkaTemplate.send(ORDER_TOPIC, orderId, order))
				.willReturn(CompletableFuture.completedFuture(sendResult));

		orderEventProducer.create(order);

		// verify that kafkaTemplate.send is called
		then(kafkaTemplate).should().send(ORDER_TOPIC, orderId, order);
	}

	@Test
	void shouldLogResult() {
		given(kafkaTemplate.send(ORDER_TOPIC, orderId, order))
				.willReturn(CompletableFuture.completedFuture(sendResult));

		orderEventProducer.create(order);

		// verify that sendResult is logged
		BDDAssertions.then(inMemoryAppender.contains("topic-partition " + topic + "-" + partition, Level.INFO))
				.isTrue();
	}

	@Test
	void shouldLogFailReason() {
		given(kafkaTemplate.send(ORDER_TOPIC, orderId, order))
				.willReturn(CompletableFuture.failedFuture(new RuntimeException("Deserved it")));

		orderEventProducer.create(order);

		// verify that send failure is logged
		BDDAssertions.then(inMemoryAppender.contains("Unable to write Order to topic", Level.WARN)).isTrue();
	}

}
