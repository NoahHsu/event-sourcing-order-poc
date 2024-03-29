package org.example.event.sourcing.order.poc.query.shipment.domain.repo;

import org.example.event.sourcing.order.poc.query.shipment.domain.entity.ShipmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "v1-shipments", collectionResourceRel = "v1-shipments", itemResourceRel = "v1-shipments")
public interface ShipmentRepository extends JpaRepository<ShipmentRecord, String> {

    @Override
    @RestResource(exported = false)
    void deleteById(String id);

    @Override
    @RestResource(exported = false)
    ShipmentRecord save(ShipmentRecord orderRecord);

}
