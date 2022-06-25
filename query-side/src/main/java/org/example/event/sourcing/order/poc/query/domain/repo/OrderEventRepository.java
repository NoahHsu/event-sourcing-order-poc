package org.example.event.sourcing.order.poc.query.domain.repo;

import org.example.event.sourcing.order.poc.query.domain.entity.OrderEventRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderEventRepository extends JpaRepository<OrderEventRecord, Long> {

    List<OrderEventRecord> findByOrderId(String orderId);

}
