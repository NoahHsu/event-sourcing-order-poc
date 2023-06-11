package org.example.event.sourcing.order.poc.query.order.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.event.sourcing.order.poc.event.model.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.List;
import java.util.function.BiConsumer;

import static org.example.event.sourcing.order.poc.event.model.OrderEvent.ORDER_REQUEUE_TOPIC;
import static org.example.event.sourcing.order.poc.event.model.OrderEvent.ORDER_TOPIC;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaConfig {

    @Bean
    DefaultErrorHandler orderRequeueEventErrorHandler(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        DeadLetterPublishingRecoverer deadLetterPublisher = getDeadLetterPublishingRecoverer(kafkaTemplate);
        return new DefaultErrorHandler(deadLetterPublisher, new FixedBackOff(60000, 2));
    }

    private static DeadLetterPublishingRecoverer getDeadLetterPublishingRecoverer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        DeadLetterPublishingRecoverer deadLetterPublisher = new DeadLetterPublishingRecoverer(kafkaTemplate);
        deadLetterPublisher.excludeHeader(DeadLetterPublishingRecoverer.HeaderNames.HeadersToAdd.EX_STACKTRACE);
        return deadLetterPublisher;
    }

    @Bean
    @Primary
    DefaultErrorHandler orderMainEventErrorHandler(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        ConsumerRecordRecoverer requeueRecoverer = recoverer(kafkaTemplate);
        return new DefaultErrorHandler(requeueRecoverer, new FixedBackOff(5000, 2));
    }


    private ConsumerRecordRecoverer recoverer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        DeadLetterPublishingRecoverer deadLetterPublisher = getDeadLetterPublishingRecoverer(kafkaTemplate);
        List<Class<? extends Throwable>> deadExceptions = DeadLetterPublishingRecoverer.defaultFatalExceptionsList();
        ConsumerRecordRecoverer result = (ConsumerRecord<?, ?> consumerRecord, Exception e) -> {
            log.warn("in orderMainEventErrorHandler e = {}", e.getCause().getClass().getSimpleName(), e.getCause());
            if (consumerRecord.topic().equals(ORDER_TOPIC) &&
                    !deadExceptions.contains(e.getCause().getClass())) {
                String toTopic = ORDER_REQUEUE_TOPIC;
                String key = (String) consumerRecord.key();
                OrderEvent message = (OrderEvent) consumerRecord.value();
                kafkaTemplate.send(toTopic, key, message);
            } else {
                deadLetterPublisher.accept(consumerRecord, e);
            }
        };
        return result;
    }

}
