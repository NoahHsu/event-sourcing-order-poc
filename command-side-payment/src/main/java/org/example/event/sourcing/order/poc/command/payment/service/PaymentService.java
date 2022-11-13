package org.example.event.sourcing.order.poc.command.payment.service;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.event.sourcing.order.poc.command.payment.producer.PaymentEventProducer;
import org.example.event.sourcing.order.poc.common.model.Payment;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEvent;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEventName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static org.example.event.sourcing.order.poc.common.model.event.PaymentEventName.*;


@Service
public class PaymentService {

    private final PaymentEventProducer paymentEventProducer;

    @Autowired
    public PaymentService(PaymentEventProducer paymentEventProducer) {
        this.paymentEventProducer = paymentEventProducer;
    }

    public Payment createPayment(Payment payment) {
        randomFail();
        Boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, CREATED));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("create payment event fail");
        }
    }

    private void randomFail() {
        if (RandomUtils.nextBoolean()) {
            throw new RuntimeException("random fail");
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
        randomFail();
        PaymentEventName event = isValid(payment) ? VALIDATED : INVALID;
        Boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, event));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("payment validated event fail");
        }
    }

    private Boolean isValid(Payment payment) {
        String failContainWord = "fail";
        return !StringUtils.containsIgnoreCase(
                payment.paymentMethod(), failContainWord);
    }

    public Payment confirmPayment(Payment payment) {
        randomFail();
        Boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, AUTHORIZED));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("payment confirmed event fail");
        }
    }

    public Payment cancelPayment(Payment payment) {
        randomFail();
        Boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, CANCELLED));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("payment canceled event fail");
        }
    }

    public Payment settlePayment(Payment payment) {
        randomFail();
        Boolean isSuccess = paymentEventProducer.create(
                getPaymentEvent(payment, SETTLED));
        if (isSuccess) {
            return payment;
        } else {
            throw new RuntimeException("payment settled event fail");
        }
    }

}
