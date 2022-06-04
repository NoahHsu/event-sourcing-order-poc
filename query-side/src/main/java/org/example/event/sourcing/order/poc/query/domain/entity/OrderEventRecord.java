package org.example.event.sourcing.order.poc.query.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDER_EVENT_RECORD")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEventRecord {

    @Id
    private String logId;

    private String orderId;

    private String fromStatus;

    private String toStatus;

    private String event;

    @CreatedDate
    private Instant createdDate;

}
