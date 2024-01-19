package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.entity.ApiLog;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnalyzerService {
    private final FileReader fileReader;
    private final ApiLogValidator validator;
    private final OutputPrinter printer;

    @Value("${analyzer.printer.frequency.amount}")
    private long frequentlyUsedUrlsAmount;

    public AnalyzerService(FileReader fileReader, ApiLogValidator validator, OutputPrinter printer) {
        this.fileReader = fileReader;
        this.validator = validator;
        this.printer = printer;
    }

    @PostConstruct
    public void init() throws IOException {
        var start = Instant.now();

        var inputData = fileReader.loadData();
        var apiLogs = validator.getValidRows(inputData)
            .stream()
            .map(ApiLog::fromDto)
            .toList();

        generateResults(apiLogs);
        generateStatistics(apiLogs);
        generateCounters(
            inputData.size(),
            apiLogs.size(),
            Duration.between(start, Instant.now()).toMillis()
        );
    }

    private void generateResults(List<ApiLog> rows) {
        var mostFrequentApiCalls = rows.stream()
            .map(it -> new ApiLog(it.httpMethod, it.endpoint))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(frequentlyUsedUrlsAmount)
            .toList();

        System.out.println("\nResults:");
        mostFrequentApiCalls.forEach(
            entry -> printer.printResult(
                entry.getKey().endpoint,
                entry.getKey().httpMethod,
                entry.getValue()
            )
        );
    }

    private void generateStatistics(List<ApiLog> rows) {
        var simultaneousApiCalls = rows.stream()
            .map(it -> new ApiLog(it.timestamp))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet();

        System.out.println("\n---- Statistics ----\nRequests per seconds:");
        simultaneousApiCalls.forEach(entry -> printer.printStatistic(entry.getKey().timestamp, entry.getValue()));
        System.out.println("------------------");
    }

    private void generateCounters(int totalRows, int validRows, long executionTime) {
        System.out.print("\n----- Counters -----");
        printer.printCounters(totalRows, validRows, executionTime);
        System.out.println("--------------------\n");
    }
}
