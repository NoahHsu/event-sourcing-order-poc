package org.example.event.sourcing.order.poc.command.payment.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.event.sourcing.order.poc.command.payment.producer.PaymentEventProducer;
import org.example.event.sourcing.order.poc.modules.common.model.Payment;
import org.example.event.sourcing.order.poc.modules.event.model.PaymentEvent;
import org.example.event.sourcing.order.poc.modules.event.model.PaymentEventName;
import org.example.event.sourcing.order.poc.modules.observation.annotation.LogInfo;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.example.event.sourcing.order.poc.modules.event.model.PaymentEventName.*;

@Service
@RequiredArgsConstructor
@LogInfo
public class PaymentService {

    private final PaymentEventProducer paymentEventProducer;

    public Payment createPayment(Payment payment) {
        boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, CREATED));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("create payment event fail");
        }
    }

    private PaymentEvent getPaymentEvent(Payment payment, PaymentEventName event) {
        return PaymentEvent.builder()
                .id(payment.id())
                .paymentMethod(payment.paymentMethod())
                .eventName(event)
                .createdDate(Instant.now())
                .updatedDate(Instant.now())
                .build();
    }

    public Payment validatePayment(Payment payment) {
        PaymentEventName event = isValid(payment) ? VALIDATED : INVALID;
        boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, event));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("payment validated event fail");
        }
    }

    private boolean isValid(Payment payment) {
        String failContainWord = "fail";
        return !StringUtils.containsIgnoreCase(
                payment.paymentMethod(), failContainWord);
    }

    public Payment confirmPayment(Payment payment) {
        boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, AUTHORIZED));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("payment confirmed event fail");
        }
    }

    public Payment cancelPayment(Payment payment) {
        boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, CANCELLED));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("payment canceled event fail");
        }
    }

    public Payment settlePayment(Payment payment) {
        boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, SETTLED));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("payment settled event fail");
        }
    }

}
