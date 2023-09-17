package org.example.event.sourcing.order.poc.external.client.toggle.flagr;

import dev.openfeature.sdk.*;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.example.event.sourcing.order.poc.external.client.toggle.flagr.model.V1EvaluationRequest;
import org.example.event.sourcing.order.poc.external.client.toggle.flagr.model.V1EvaluationResponse;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class OpenFlagrProvider implements FeatureProvider {

    private final FlagrClient flagrClient;

    private final Set<String> defaultOnToggleKeys = Set.of("On", "1", "true");

    @Override
    public Metadata getMetadata() {
        return () -> "OpenFlagrProvider";
    }

    @Override
    public List<Hook> getProviderHooks() {
        return FeatureProvider.super.getProviderHooks();
    }

    @Override
    public ProviderEvaluation<Boolean> getBooleanEvaluation(String key, Boolean defaultValue, EvaluationContext ctx) {

        V1EvaluationRequest request = buildRequest(key, ctx);

        V1EvaluationResponse response = flagrClient.evaluate(request);
        String answerVariant = response.variantKey() == null
                ? ""
                : response.variantKey().toLowerCase();
        boolean isOn = defaultOnToggleKeys.contains(answerVariant);

        return ProviderEvaluation.<Boolean>builder()
                .value(isOn)
                .variant(response.variantKey())
                .build();
    }

    private static V1EvaluationRequest buildRequest(String key, EvaluationContext ctx) {
        String entityID = ctx.getTargetingKey();
        var context = ctx.asObjectMap();

        return V1EvaluationRequest.builder()
                .flagKey(key)
                .entityContext(context)
                .entityID(StringUtils.isBlank(entityID) ? null : entityID)
                .enableDebug(false)
                .build();
    }

    @Override
    public ProviderEvaluation<String> getStringEvaluation(String key, String defaultValue, EvaluationContext ctx) {
        V1EvaluationRequest request = buildRequest(key, ctx);
        V1EvaluationResponse response = flagrClient.evaluate(request);
        String answerVariant = response.variantKey() == null
                ? ""
                : response.variantKey();

        return ProviderEvaluation.<String>builder()
                .value(answerVariant)
                .build();
    }

    @Override
    public ProviderEvaluation<Integer> getIntegerEvaluation(String key, Integer defaultValue, EvaluationContext ctx) {
        return null;
    }

    @Override
    public ProviderEvaluation<Double> getDoubleEvaluation(String key, Double defaultValue, EvaluationContext ctx) {
        return null;
    }

    @Override
    public ProviderEvaluation<Value> getObjectEvaluation(String key, Value defaultValue, EvaluationContext ctx) {
        return null;
    }

}
