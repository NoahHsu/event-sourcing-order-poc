package org.example.event.sourcing.order.poc.query.shipment.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.event.sourcing.order.poc.common.model.event.ShipmentEventName;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "SHIPMENT_RECORD")
@EntityListeners(AuditingEntityListener.class)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentRecord {

    @Id
    private String shipmentId;

    private String shipmentMethod;

    private ShipmentEventName state;

    private Instant createDate;

    private Instant updateDate;

}
