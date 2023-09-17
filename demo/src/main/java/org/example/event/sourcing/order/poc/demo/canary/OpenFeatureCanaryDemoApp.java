package org.example.event.sourcing.order.poc.demo.canary;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.MutableContext;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.external.client.toggle.FeatureToggleApiProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class OpenFeatureCanaryDemoApp implements CommandLineRunner {

    private static final String FLAG_KEY = "canary-flag";

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private FeatureToggleApiProvider featureToggleApiProvider;

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(OpenFeatureCanaryDemoApp.class, args);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        Client client = featureToggleApiProvider.getFlagrApiClient();

        String isNext;

        do {
            iteration(client, 500);
            System.out.println("enter Y to start next iteration (press other to exit)");
            isNext = new Scanner(System.in).next();
        } while ("Y".equalsIgnoreCase(isNext));

        SpringApplication.exit(context);
    }

    private static void iteration(Client client, int times) throws InterruptedException {
        int i = 0, j = 0;
        int v1 = 0, v2 = 0;

        int maxColumn = 50;
        int maxRow = times/ maxColumn;

        while (j < maxRow) {
            UUID userId = UUID.randomUUID();
            MutableContext ctx = new MutableContext(userId.toString());

            String version = client.getStringValue(FLAG_KEY, "v1", ctx);

            String message = " ";

            switch (version) {
                case "v1":
                    message = "+";
                    v1++;
                    break;
                case "v2":
                    message = "-";
                    v2++;
                    break;
                default:
            }

            System.out.print(message);

            i++;

            if (i == maxColumn) {
                i = 0;
                j++;
                System.out.println("");
            }

            Thread.sleep(20);
        }

        System.out.println("==== This Iteration (total = " + times + ")====");
        System.out.println(String.format("v1 : %d (%,.2f%%); v2: %d (%,.2f%%)",
                v1, (float) v1 / times * 100,
                v2, (float) v2 / times * 100));
        System.out.println("====    End Of This Iteration    ====");

    }
}
