package org.example.event.sourcing.order.poc.query.domain.repo;

import org.example.event.sourcing.order.poc.query.domain.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderRecord, String> {
}
