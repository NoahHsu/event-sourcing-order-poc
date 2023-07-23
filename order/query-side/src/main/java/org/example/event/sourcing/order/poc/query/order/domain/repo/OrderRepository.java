package org.example.event.sourcing.order.poc.query.order.domain.repo;

import org.example.event.sourcing.order.poc.query.order.domain.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "v1-orders", collectionResourceRel = "v1-orders", itemResourceRel = "v1-orders")
@CrossOrigin(origins = "http://localhost:3000") // to allow backstage embeded swaggerUI to try API
public interface OrderRepository extends JpaRepository<OrderRecord, String> {

    @Override
    @RestResource(exported = false)
    void deleteById(String id);

    @Override
    @RestResource(exported = false)
    OrderRecord save(OrderRecord orderRecord);

}
