package org.example.event.sourcing.order.poc.demo.canary;

import dev.openfeature.sdk.Client;
import lombok.extern.slf4j.Slf4j;
import org.example.event.sourcing.order.poc.external.client.toggle.FeatureToggleApiProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class DemoApp implements CommandLineRunner {

    @Autowired
    private FeatureToggleApiProvider featureToggleApiProvider;

    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        log.info("STARTING THE APPLICATION");
        SpringApplication.run(DemoApp.class, args);
        log.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {
        Client client = featureToggleApiProvider.getFlagrApiClient();
        int iterationCount = 0;
        String isNext = "Y";
        printInstruction();
        String whichDemo = new Scanner(System.in).next();

        while ("Y".equalsIgnoreCase(isNext)) {
            iterationCount++;

            switch (whichDemo) {
                case "A": // AB-Testing
                    OpenFeatureABTestDemo.iteration(client, "Asia", 200, iterationCount);
                    OpenFeatureABTestDemo.iteration(client, "Europe", 200, iterationCount);
                    OpenFeatureABTestDemo.iteration(client, "Africa", 100, iterationCount);
                    break;
                case "B": // Blue/Green
                    OpenFeatureBlueGreenDemo.iteration(client, 100, iterationCount);
                    break;
                case "C": // Canary
                    OpenFeatureCanaryDemo.iteration(client, 500, iterationCount);
                    break;
                case "S": // Shadow
                    OpenFeatureShadowDemo.iteration(client, 100, iterationCount);
                    break;
                default:
                    isNext = "N";
                    break;
            }

            if ("Y".equalsIgnoreCase(isNext)) {
                printInstruction();
                whichDemo = new Scanner(System.in).next();
            }
        }
        System.out.println("End Demo.");
        SpringApplication.exit(context);
    }

    private static void printInstruction() {
        System.out.println("");
        System.out.println("enter options to start next iteration (other words to exit)");
        System.out.println("A for AB-Testing");
        System.out.println("B for Blue/Green");
        System.out.println("C for Canary");
        System.out.println("S for Shadow");
    }
}
