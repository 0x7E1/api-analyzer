package io.skai.platform.apianalyzer.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OutputPrinter {

    void printResult(String path, HttpMethod method, long invocationsCount) {
        if (invocationsCount > 1) {
            System.out.printf("%s – %s – %d times%n", path, method.name(), invocationsCount);
        } else {
            System.out.printf("%s – %s – %d time%n", path, method.name(), invocationsCount);
        }
    }

    void printStatistic(LocalDateTime dateTime, long requestsCount) {
        var timestamp = dateTime.toString().substring(0, 19);
        if (requestsCount > 1) {
            System.out.printf("%s – %d requests%n", timestamp, requestsCount);
        } else {
            System.out.printf("%s – %d request%n", timestamp, requestsCount);
        }
    }

    void printCounters(int total, int valid, long executionTime) {
        System.out.println(
            "\nTotal rows – " + total +
            "\nValid rows – " + valid +
            "\nProcessed total time – " + executionTime / 1000.0 + " seconds"
        );
    }
}
