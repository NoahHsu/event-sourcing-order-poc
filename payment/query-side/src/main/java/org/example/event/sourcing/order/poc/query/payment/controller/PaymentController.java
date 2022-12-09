package org.example.event.sourcing.order.poc.query.payment.controller;

import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.query.payment.domain.entity.PaymentRecord;
import org.example.event.sourcing.order.poc.query.payment.service.PaymentReadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentReadService paymentReadService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentRecord> getPayments() {
        return paymentReadService.getPayment();
    }

    @GetMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentRecord getPayment(@PathVariable String paymentId) {
        return paymentReadService.getPayment(paymentId);
    }

}
