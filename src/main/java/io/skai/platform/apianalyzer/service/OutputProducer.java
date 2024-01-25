package io.skai.platform.apianalyzer.service;

import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;

public interface OutputProducer {
    void printResult(String path, HttpMethod method, long invocationsCount);
    void printStatistic(LocalDateTime dateTime, long requestsCount);
    void printCounters(int total, int valid, long executionTime);
}
