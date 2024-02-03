package org.example.event.sourcing.order.poc.query.payment.domain.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.modules.event.model.PaymentEvent;
import org.example.event.sourcing.order.poc.modules.event.model.PaymentEventName;
import org.example.event.sourcing.order.poc.query.payment.domain.entity.PaymentRecord;
import org.example.event.sourcing.order.poc.query.payment.domain.handler.PaymentRecordHandler;
import org.example.event.sourcing.order.poc.query.payment.domain.repo.PaymentRepository;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentRecordHandlerImpl implements PaymentRecordHandler {

    private final PaymentRepository paymentRepository;

    @Override
    public void onEvent(PaymentEvent event) {
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
                break;
            default:
                throw new RuntimeException("unsupported event");
        }
    }

    private void createPayment(PaymentEvent event) {
        String paymentId = event.id();
        log.info("Create payment id = {}.", paymentId);
        boolean isExsit = paymentRepository.existsById(paymentId);
        if (!isExsit) {
            PaymentRecord entity = PaymentRecord.builder()
                    .paymentId(event.id())
                    .paymentMethod(event.paymentMethod())
                    .status(PaymentEventName.CREATED)
                    .createdDate(event.createdDate())
                    .updatedDate(event.updatedDate())
                    .build();
            PaymentRecord result = paymentRepository.save(entity);
            log.info("save payment = {}", result);
        } else {
            log.warn("payment id = {} had been created.", paymentId);
        }

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
