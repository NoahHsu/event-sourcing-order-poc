package org.example.event.sourcing.order.poc.external.client.toggle;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.example.event.sourcing.order.poc.external.client.toggle.flagr.FlagrClient;
import org.example.event.sourcing.order.poc.external.client.toggle.flagr.OpenFlagrProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeatureToggleApiProvider implements InitializingBean {
    @Autowired
    FlagrClient flagrClient;

    OpenFeatureAPI api = OpenFeatureAPI.getInstance();

    @Override
    public void afterPropertiesSet() throws Exception {
        OpenFlagrProvider openFlagrProvider = new OpenFlagrProvider(flagrClient);
        api.setProvider(openFlagrProvider);
    }

    public Client getFlagrApiClient() {
        return api.getClient();
    }

}
