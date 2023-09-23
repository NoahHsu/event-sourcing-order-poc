package org.example.event.sourcing.order.poc.demo.canary;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.MutableContext;

import java.util.UUID;

import static org.example.event.sourcing.order.poc.demo.canary.DemoHelper.*;

public final class OpenFeatureABTestDemo {

    private static final String FLAG_KEY = "ab-test";

    public static void iteration(Client client, String region, int times, int iterationCount) {
        printStartIteration(iterationCount, region);

        int i = 0, j = 0;
        int v1 = 0, v2 = 0;

        int maxColumn = 50;
        int maxRow = times / maxColumn;
        long startTime = System.nanoTime();

        while (j < maxRow) {
            UUID userId = UUID.randomUUID();
            MutableContext ctx = new MutableContext(userId.toString());
            ctx.add("region", region);

            String version = client.getStringValue(FLAG_KEY, "v1", ctx);

            String message = "";
            switch (version) {
                case "v1":
                    message = v1Feature();
                    v1++;
                    break;
                case "v2":
                    message = v2Feature();
                    v2++;
                    break;
                default:
            }
            System.out.print(message);

            i++;
            // prettier format
            if (i == maxColumn) {
                i = 0;
                j++;
                System.out.println("");
            }

        }

        long duration = System.nanoTime() - startTime;
        printReport(region, times, iterationCount, v1, v2, (double) duration);
    }

}
