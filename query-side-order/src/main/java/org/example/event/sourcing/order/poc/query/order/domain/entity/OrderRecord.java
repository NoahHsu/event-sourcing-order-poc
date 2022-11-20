package org.example.event.sourcing.order.poc.query.order.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "ORDER_RECORD")
@EntityListeners(AuditingEntityListener.class)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRecord {

    @Id
    private String orderId;

    private OrderStatus status;

    private Instant createdDate;

    private Instant updatedDate;

}
