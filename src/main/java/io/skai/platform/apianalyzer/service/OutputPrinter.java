package io.skai.platform.apianalyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class OutputPrinter {
    private final FileReader fileReader;

    @Value("${frequently.used.urls.amount}")
    private Integer frequentlyUsedUrlsAmount;

    public OutputPrinter(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public void generateAnalytics() {
        System.out.println("\nResults:");
        for (int i = 0; i < frequentlyUsedUrlsAmount; i++) {
            printResult("/save", "POST", 15);
        }

        System.out.println("\n---- Statistics ----\nRequests per seconds:");
        for (int i = 0; i < 5; i++) {
            printStatistic("28/07/2006:10:25:04".substring(0, 19), 1);
        }

        var start = Instant.now();
        var finish = Instant.now();
        printCounters(123, 120, Duration.between(start, finish).toSeconds());
    }

    private void printResult(String path, String method, int invocationsAmount) {
        if (invocationsAmount > 1) {
            System.out.printf("%s – %s – %d times%n", path, method, invocationsAmount);
        } else {
            System.out.printf("%s – %s – %d time%n", path, method, invocationsAmount);
        }
    }

    private void printStatistic(String timestamp, int requestsAmount) {
        if (requestsAmount > 1) {
            System.out.printf("%s – %d requests%n", timestamp, requestsAmount);
        } else {
            System.out.printf("%s – %d request%n", timestamp, requestsAmount);
        }
    }

    private void printCounters(int total, int valid, long executionTime) {
        var result =
            "\n---- Counters ----" +
            "\nTotal rows – " + total +
            "\nValid rows – " + valid +
            "\nProcessed total time – " + executionTime + " sec" +
            "\n------------------\n";

        System.out.println(result);
    }
}
