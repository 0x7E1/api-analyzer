package io.skai.platform.apianalyzer.service.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.exception.ResourceNotFoundException;
import io.skai.platform.apianalyzer.model.AnalyticsResult;
import io.skai.platform.apianalyzer.model.ApiLog;
import io.skai.platform.apianalyzer.service.AnalyzationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RestApiAnalyzationService implements AnalyzationService {
    private final WebCsvFileLoader fileLoader;
    private final ConsoleAnalyzationService consoleService;
    private final Map<UUID, byte[]> resourceData = new HashMap<>();

    @Value("${analyzer.results.limit}")
    public long resultsLimit;

    public RestApiAnalyzationService(
        WebCsvFileLoader webCsvFileLoader,
        ConsoleAnalyzationService consoleAnalyzationService
    ) {
        fileLoader = webCsvFileLoader;
        consoleService = consoleAnalyzationService;
    }

    public UUID addResource(MultipartFile resource) {
        try {
            var token = UUID.randomUUID();
            resourceData.put(token, resource.getBytes());

            return token;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AnalyticsResult> getResults(UUID token) {
        var start = Instant.now();

        var rawData = resourceData.get(token);
        if (rawData == null) {
            throw new ResourceNotFoundException("Resource with such token doesn't exist");
        }

        fileLoader.setResource(new ByteArrayInputStream(rawData));
        List<ApiLogDto> inputData = fileLoader.loadApiLogs();
        if (inputData != null) {
            var validRows = getValidRows(inputData).stream()
                .map(ApiLog::fromDto)
                .toList();
            var results = generateResults(validRows);
            generateStatistics(validRows);
            generateCounters(inputData.size(), validRows.size(), Duration.between(start, Instant.now()).toMillis());

            return results;
        }

        return null;
    }

    @Override
    public List<AnalyticsResult> generateResults(List<ApiLog> rows) {
        return calculateMostFrequentApiCalls(rows, resultsLimit).stream()
            .map(it -> AnalyticsResult.builder()
                .endpoint(it.getKey().endpoint())
                .httpMethod(it.getKey().httpMethod())
                .invocationsCount(it.getValue().intValue())
                .build())
            .toList();
    }

    @Override
    public void generateStatistics(List<ApiLog> rows) {
        consoleService.generateStatistics(rows);
    }

    @Override
    public void generateCounters(int totalRows, int validRows, long executionTime) {
        consoleService.generateCounters(totalRows, validRows, executionTime);
    }
}
