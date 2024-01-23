package io.skai.platform.apianalyzer.service.impl;

import com.google.common.collect.Lists;
import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.exception.ResourceNotFoundException;
import io.skai.platform.apianalyzer.model.AnalyticsResult;
import io.skai.platform.apianalyzer.model.ApiLog;
import io.skai.platform.apianalyzer.model.enums.ValidationType;
import io.skai.platform.apianalyzer.service.AnalyzationService;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RestApiAnalyzationService implements AnalyzationService {
    private final WebCsvFileLoader fileLoader;
    private final ConsoleAnalyzationService consoleService;
    private final Map<UUID, byte[]> resourceData = new HashMap<>();

    @Value("${analyzer.printer.frequency.amount}")
    private long frequentlyUsedUrlsAmount;

    public RestApiAnalyzationService(
        WebCsvFileLoader webCsvFileLoader,
        ConsoleAnalyzationService consoleAnalyzationService
    ) {
        fileLoader = webCsvFileLoader;
        consoleService = consoleAnalyzationService;
    }

    public void addResource(UUID token, MultipartFile resource) {
        try {
            resourceData.put(token, resource.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AnalyticsResult> getResults(UUID token) {
        var start = Instant.now();

        var rawData = resourceData.get(token);
        if (rawData == null) {
            throw new ResourceNotFoundException("Resource with such token doesn't exist.");
        }

        fileLoader.setResource(new ByteArrayInputStream(rawData));
        List<ApiLogDto> inputData = fileLoader.loadApiLogs();
        if (inputData == null) {
            return List.of();
        }

        var validApiLogs = getValidApiLogs(inputData);
        var results = calculateResults(validApiLogs);
        calculateStatistics(validApiLogs);
        calculateCounters(inputData.size(), validApiLogs.size(), Duration.between(start, Instant.now()).toMillis());

        return results;
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

        return mostFrequentApiCalls.stream()
            .map(it -> AnalyticsResult.fromApiLog(it.getKey(), it.getValue().intValue()))
            .toList();
    }

    @Override
    public void calculateStatistics(List<ApiLog> rows) {
        consoleService.calculateStatistics(rows);
    }

    @Override
    public void calculateCounters(int totalRows, int validRows, long executionTime) {
        consoleService.calculateCounters(totalRows, validRows, executionTime);
    }

    private List<ApiLog> getValidApiLogs(List<ApiLogDto> inputData) {
        ArrayList<ApiLogDto> invalidRows = Lists.newArrayList();
        ApiLogValidator validator;

        for (var validationType : ValidationType.values()) {
            validator = setValidationStrategy(validationType);

            for (ApiLogDto dto : inputData) {
                if (validator.isInvalid(dto)) {
                    invalidRows.add(dto);
                }
            }
        }

        return inputData.stream()
            .filter(it -> !invalidRows.contains(it))
            .map(ApiLog::fromDto)
            .toList();
    }
}
