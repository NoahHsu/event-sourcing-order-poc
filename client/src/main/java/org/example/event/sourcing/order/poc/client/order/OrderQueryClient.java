package org.example.event.sourcing.order.poc.client.order;

import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.example.event.sourcing.order.poc.client.order.model.V1Order;
import org.springframework.hateoas.EntityModel;

public class OrderQueryClient {

    private final OrderQueryStub orderQueryStub;

    public OrderQueryClient(Feign.Builder builder) {
        OrderQueryStub feign = builder.target(OrderQueryStub.class, "http://localhost:8083");
        this.orderQueryStub = feign;
    }

    public V1Order get(String id) {
        return orderQueryStub.get(id).getContent();
    }

    private interface OrderQueryStub {
        String BASE_PATH = "/api/v1-orders";

        @RequestLine("GET " + BASE_PATH + "/{id}")
        @Headers("Content-Type: application/json")
        EntityModel<V1Order> get(@Param("id") String id);

    }


}
