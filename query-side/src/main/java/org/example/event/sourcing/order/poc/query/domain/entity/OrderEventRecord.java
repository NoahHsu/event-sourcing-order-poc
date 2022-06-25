package org.example.event.sourcing.order.poc.query.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

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

    private String fromStatus;

    private String toStatus;

    private String event;

    @CreatedDate
    private Instant createdDate;

}
