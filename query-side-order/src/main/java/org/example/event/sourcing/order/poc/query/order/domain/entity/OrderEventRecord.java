package org.example.event.sourcing.order.poc.query.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.event.sourcing.order.poc.common.model.event.OrderEventName;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ORDER_EVENT_RECORD")
@EntityListeners(AuditingEntityListener.class)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEventRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;

    private String orderId;

    private OrderStatus fromStatus;

    private OrderStatus toStatus;

    private OrderEventName eventName;

    @CreatedDate
    private Instant createdDate;

}
