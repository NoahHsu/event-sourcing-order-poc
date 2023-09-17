package org.example.event.sourcing.order.poc.external.client.toggle.flagr.model;

import lombok.Builder;

import java.util.List;
import java.util.Map;

public record V1EvaluationResponse(
        double flagID,
        String flagKey,
        double flagSnapshotID,
        double segmentID,
        double variantID,
        String variantKey,
        Map<String, Object> variantAttachment,
        V1EvaluationRequest evalContext,

        String timestamp,
        FlagrEvaluationDebugLog evalDebugLog

) {
    @Builder
    public V1EvaluationResponse {
    }
    
    private record FlagrEvaluationDebugLog(
            List<FlagrSegmentDebugLog> segmentDebugLogs
    ) {
    }

    private record FlagrSegmentDebugLog(
            double segmentID,
            String msg
    ) {
    }

}
