package org.example.event.sourcing.order.poc.external.client.toggle.flagr.model;

import lombok.Builder;

import java.util.List;
import java.util.Map;

public record V1EvaluationRequest(
        String entityID,
        String entityType,

        Map<String, Object> entityContext,
        boolean enableDebug,
        Double flagID,
        String flagKey,
        List<String> flagTags,
        String flagTagsOperator
) {

    @Builder
    public V1EvaluationRequest {
    }

}
