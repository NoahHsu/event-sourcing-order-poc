package org.example.event.sourcing.order.poc.query.payment.domain.repo;

import org.example.event.sourcing.order.poc.query.payment.domain.entity.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "v1-payments", collectionResourceRel = "v1-payments", itemResourceRel = "v1-payments")
public interface PaymentRepository extends JpaRepository<PaymentRecord, String> {

    @Override
    @RestResource(exported = false)
    void deleteById(String id);

    @Override
    @RestResource(exported = false)
    PaymentRecord save(PaymentRecord paymentRecord);

}
