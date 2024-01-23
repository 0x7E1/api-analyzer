package io.skai.platform.apianalyzer.service;

import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;

public interface OutputProducer {
    void generateResult(String path, HttpMethod method, long invocationsCount);
    void generateStatistic(LocalDateTime dateTime, long requestsCount);
    void generateCounters(int total, int valid, long executionTime);
}
