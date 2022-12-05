package org.example.event.sourcing.order.poc.query.payment.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.event.sourcing.order.poc.common.model.event.PaymentEventName;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "PAYMENT_RECORD")
@EntityListeners(AuditingEntityListener.class)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRecord {

    @Id
    private String paymentId;

    private String paymentMethod;

    private PaymentEventName status;

    private Instant createdDate;

    private Instant updatedDate;

}
