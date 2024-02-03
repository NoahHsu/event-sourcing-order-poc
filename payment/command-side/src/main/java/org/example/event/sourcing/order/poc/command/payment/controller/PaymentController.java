package org.example.event.sourcing.order.poc.command.payment.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.command.payment.service.PaymentService;
import org.example.event.sourcing.order.poc.modules.common.model.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Observed
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @PostMapping("/valid")
    @ResponseStatus(HttpStatus.OK)
    public Payment validatePayment(@RequestBody Payment payment) {
        return paymentService.validatePayment(payment);
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public Payment confirmPayment(@RequestBody Payment payment) {
        return paymentService.confirmPayment(payment);
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public Payment cancelPayment(@RequestBody Payment payment) {
        return paymentService.cancelPayment(payment);
    }

    @PostMapping("/settle")
    @ResponseStatus(HttpStatus.OK)
    public Payment settlePayment(@RequestBody Payment payment) {
        return paymentService.settlePayment(payment);
    }

}
