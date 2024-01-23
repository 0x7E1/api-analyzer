package io.skai.platform.apianalyzer.service.impl;

import com.google.common.collect.Lists;
import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.model.AnalyticsResult;
import io.skai.platform.apianalyzer.model.ApiLog;
import io.skai.platform.apianalyzer.model.enums.ValidationType;
import io.skai.platform.apianalyzer.service.AnalyzationService;
import io.skai.platform.apianalyzer.service.OutputProducer;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConsoleAnalyzationService implements AnalyzationService {
    private final LocalCsvFileLoader localFileLoader;
    private final OutputProducer producer;

    @Value("${analyzer.printer.frequency.amount}")
    private long frequentlyUsedUrlsAmount;

    public ConsoleAnalyzationService(LocalCsvFileLoader localCsvFileLoader, ConsolePrinter consolePrinter) {
        localFileLoader = localCsvFileLoader;
        producer = consolePrinter;
    }

    public void run(ConfigurableApplicationContext context) {
        var start = Instant.now();
        ApiLogValidator validator;

        List<ApiLogDto> inputData = localFileLoader.loadApiLogs();
        ArrayList<ApiLogDto> invalidRows = Lists.newArrayList();

        for (var validationType : ValidationType.values()) {
            validator = setValidationStrategy(validationType);

            for (ApiLogDto dto : inputData) {
                if (validator.isInvalid(dto)) {
                    invalidRows.add(dto);
                }
            }
        }

        var validRows = inputData.stream()
            .filter(it -> !invalidRows.contains(it))
            .map(ApiLog::fromDto)
            .toList();

        calculateResults(validRows);
        calculateStatistics(validRows);
        calculateCounters(
            inputData.size(),
            validRows.size(),
            Duration.between(start, Instant.now()).toMillis()
        );

        SpringApplication.exit(context, () -> 0);
    }

    @Override
    public List<AnalyticsResult> calculateResults(List<ApiLog> rows) {
        var mostFrequentApiCalls = rows.stream()
            .map(it -> ApiLog.builder()
                .httpMethod(it.httpMethod())
                .endpoint(it.endpoint())
                .build())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(frequentlyUsedUrlsAmount)
            .toList();

        System.out.println("\nResults:");
        mostFrequentApiCalls.forEach(
            entry -> producer.generateResult(
                entry.getKey().endpoint(),
                entry.getKey().httpMethod(),
                entry.getValue()
            )
        );
        // Do not need returned value
        return null;
    }

    @Override
    public void calculateStatistics(List<ApiLog> rows) {
        var simultaneousApiCalls = rows.stream()
            .map(it -> ApiLog.builder()
                .timestamp(it.timestamp())
                .build())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet();

        System.out.println("\n---- Statistics ----\nRequests per seconds:");
        simultaneousApiCalls.forEach(
            entry -> producer.generateStatistic(entry.getKey().timestamp(), entry.getValue())
        );
        System.out.println("------------------");
    }

    @Override
    public void calculateCounters(int totalRows, int validRows, long executionTime) {
        System.out.print("\n----- Counters -----");
        producer.generateCounters(totalRows, validRows, executionTime);
        System.out.println("--------------------\n");
    }
}
