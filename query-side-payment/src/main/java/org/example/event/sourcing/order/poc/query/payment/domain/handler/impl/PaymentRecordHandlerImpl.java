package org.example.event.sourcing.order.poc.query.payment.domain.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEventName;
import org.example.event.sourcing.order.poc.query.payment.domain.entity.PaymentRecord;
import org.example.event.sourcing.order.poc.query.payment.domain.handler.PaymentRecordHandler;
import org.example.event.sourcing.order.poc.query.payment.domain.repo.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentRecordHandlerImpl implements PaymentRecordHandler {

    private final PaymentRepository paymentRepository;

    @Override
    public CompletableFuture<Void> onEvent(PaymentEvent event) {
        return CompletableFuture.runAsync(() -> {
            switch (event.eventName()) {
                case CREATED:
                    createPayment(event);
                    break;
                case INVALID:
                case VALIDATED:
                case UNAUTHORIZED:
                case AUTHORIZED:
                case CANCELLED:
                case SETTLED:
                    updatePayment(event);
                default:
                    throw new RuntimeException("unsupported event");
            }
        });
    }

    private void createPayment(PaymentEvent event) {
        log.info("Create payment id = {}.", event.id());
        PaymentRecord entity = PaymentRecord.builder()
                .paymentId(event.id())
                .paymentMethod(event.paymentMethod())
                .status(PaymentEventName.CREATED)
                .createdDate(event.createdDate())
                .updatedDate(event.updatedDate())
                .build();
        paymentRepository.save(entity);
        log.info("save payment success");
    }

    private void updatePayment(PaymentEvent event) {
        log.info("update try by {}", event);
        paymentRepository.findById(event.id()).ifPresent(paymentRecord -> {
            paymentRecord.setStatus(event.eventName());
            paymentRepository.save(paymentRecord);
            log.info("update success. id ={}", paymentRecord.getPaymentId());
        });
    }

}
