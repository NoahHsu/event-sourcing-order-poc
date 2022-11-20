package org.example.event.sourcing.order.poc.query.shipment.domain.repo;

import org.example.event.sourcing.order.poc.query.shipment.domain.entity.ShipmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<ShipmentRecord, String> {
}
