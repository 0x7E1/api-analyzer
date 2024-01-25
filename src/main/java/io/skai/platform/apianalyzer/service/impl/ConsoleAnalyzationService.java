package io.skai.platform.apianalyzer.service.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.model.AnalyticsResult;
import io.skai.platform.apianalyzer.model.ApiLog;
import io.skai.platform.apianalyzer.service.AnalyzationService;
import io.skai.platform.apianalyzer.service.OutputProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConsoleAnalyzationService implements AnalyzationService {
    private final LocalCsvFileLoader localFileLoader;
    private final OutputProducer producer;

    @Value("${analyzer.results.limit}")
    private long resultsLimit;

    public ConsoleAnalyzationService(LocalCsvFileLoader localCsvFileLoader, ConsolePrinter consolePrinter) {
        localFileLoader = localCsvFileLoader;
        producer = consolePrinter;
    }

    public void run(ConfigurableApplicationContext context) {
        var start = Instant.now();

        List<ApiLogDto> inputData = localFileLoader.loadApiLogs();
        if (inputData != null) {
            var validRows = getValidRows(inputData).stream()
                .map(ApiLog::fromDto)
                .toList();
            generateResults(validRows);
            generateStatistics(validRows);
            generateCounters(
                inputData.size(),
                validRows.size(),
                Duration.between(start, Instant.now()).toMillis()
            );
        }

        SpringApplication.exit(context, () -> 0);
    }

    @Override
    public List<AnalyticsResult> generateResults(List<ApiLog> rows) {
        var mostFrequentApiCalls = calculateMostFrequentApiCalls(rows, resultsLimit);

        System.out.println("\nResults:");
        mostFrequentApiCalls.forEach(
            entry -> producer.printResult(
                entry.getKey().endpoint(),
                entry.getKey().httpMethod(),
                entry.getValue()
            )
        );
        // Do not need to return value
        return null;
    }

    @Override
    public void generateStatistics(List<ApiLog> rows) {
        var simultaneousApiCalls = rows.stream()
            .map(it -> ApiLog.builder()
                .timestamp(it.timestamp())
                .build())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet();

        System.out.println("\n---- Statistics ----\nRequests per seconds:");
        simultaneousApiCalls.forEach(
            entry -> producer.printStatistic(entry.getKey().timestamp(), entry.getValue())
        );
        System.out.println("------------------");
    }

    @Override
    public void generateCounters(int totalRows, int validRows, long executionTime) {
        System.out.print("\n----- Counters -----");
        producer.printCounters(totalRows, validRows, executionTime);
        System.out.println("--------------------\n");
    }
}
