package org.example.event.sourcing.order.poc.demo.canary;

import org.apache.commons.lang3.StringUtils;

public final class DemoHelper {

    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[0:32m";
    private static final String BLUE = "\033[0;34m";
    private static final String PURPLE = "\033[0;35m";

    public static void printStartIteration(int iterationCount) {
        printStartIteration(iterationCount, "");
    }

    public static void printStartIteration(int iterationCount, String otherNote) {
        System.out.println("");
        otherNote = StringUtils.isBlank(otherNote) ? "" : otherNote + " ";
        System.out.printf("Start %sIteration %d ...%n", otherNote, iterationCount);
    }

    public static String v1Feature() {
        return BLUE + "o" + RESET;
    }

    public static String v2Feature() {
        return GREEN + "x" + RESET;
    }

    public static void printReport(int times, int iterationCount, int v1, int v2, double duration) {
        printReport(null, times, iterationCount, v1, v2, duration);
    }

    public static void printReport(String otherNote, int times, int iterationCount, int v1, int v2, double duration) {
        System.out.println();
        System.out.println("=============================================");
        if (StringUtils.isBlank(otherNote)) {
            System.out.printf("|\tIteration %d Report (total = %d)\t\t|%n", iterationCount, times);
        } else {
            System.out.printf("|\t%sIteration %d Report (total = %d)\t|%n", otherNote+" ", iterationCount, times);
        }
        System.out.println("=============================================");
        System.out.printf("|\t" + BLUE + "v1:\t%d\t(%,.3f%%)" + RESET + "\t\t\t\t\t\t|%n", v1, (float) v1 / times * 100);
        System.out.printf("|\t" + GREEN + "v2:\t%d\t(%,.3f%%)" + RESET + "\t\t\t\t\t\t|%n", v2, (float) v2 / times * 100);
        System.out.printf("|\t" + PURPLE + "Execution Time: %,.2f Sec" + RESET + "\t\t\t\t|%n", (duration / 1000000000L));
        System.out.println("=============================================");
    }

}
