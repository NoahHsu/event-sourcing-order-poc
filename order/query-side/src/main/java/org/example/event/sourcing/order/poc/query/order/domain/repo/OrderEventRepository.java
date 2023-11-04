package org.example.event.sourcing.order.poc.query.order.domain.repo;

import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderEventRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "v1-orders-log", collectionResourceRel = "v1-orders-log", itemResourceRel = "v1-orders-log")
public interface OrderEventRepository extends JpaRepository<OrderEventRecord, Long> {

    @RestResource(path = "order-id")
    List<OrderEventRecord> findByOrderId(String orderId);

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

    @Override
    @RestResource(exported = false)
    OrderEventRecord save(OrderEventRecord orderRecord);

}
