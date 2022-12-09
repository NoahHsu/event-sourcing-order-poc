package org.example.event.sourcing.order.poc.query.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.query.payment.domain.entity.PaymentRecord;
import org.example.event.sourcing.order.poc.query.payment.domain.repo.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentReadService {
    
    private final PaymentRepository paymentRepository;

    public List<PaymentRecord> getPayment() {
        return paymentRepository.findAll();
    }

    public PaymentRecord getPayment(String paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow();
    }

}
