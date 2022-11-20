package org.example.event.sourcing.order.poc.query.payment.domain.repo;

import org.example.event.sourcing.order.poc.query.payment.domain.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentRecord, String> {
}
