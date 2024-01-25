package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.BaseApiAnalyzerTest;
import io.skai.platform.apianalyzer.model.ApiLog;
import io.skai.platform.apianalyzer.service.impl.ConsoleAnalyzationService;
import io.skai.platform.apianalyzer.service.impl.RestApiAnalyzationService;
import io.skai.platform.apianalyzer.service.impl.WebCsvFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class AnalyzationServiceTest extends BaseApiAnalyzerTest {
    WebCsvFileLoader fileLoaderMock;
    ConsoleAnalyzationService consoleAnalyzationServiceMock;

    AnalyzationService fixture;

    @BeforeEach
    void setUp() {
        fileLoaderMock = mock(WebCsvFileLoader.class);
        consoleAnalyzationServiceMock = mock(ConsoleAnalyzationService.class);

        fixture = new RestApiAnalyzationService(fileLoaderMock, consoleAnalyzationServiceMock);
    }

    @Test
    void getValidRows_returnsOnlyValidApiLogDtos() {
        var result = fixture.getValidRows(generateApiLogDtos());

        assertFalse(result.isEmpty());
        assertFalse(result.contains(INVALID_API_LOG_DTO));
        assertTrue(result.contains(VALID_API_LOG_DTO));
        assertEquals(VALID_API_DTOS_COUNT, result.size());
    }

    @Test
    void calculateMostFrequentApiCalls_returnsSortedAndLimitedResult() {
        var validRows =
            fixture.getValidRows(generateApiLogDtos()).stream()
                .map(ApiLog::fromDto)
                .toList();

        assertFalse(validRows.isEmpty());

        var result = fixture.calculateMostFrequentApiCalls(validRows, testResultsLimit);

        assertFalse(result.isEmpty());
        assertTrue(result.size() <= testResultsLimit);
    }

    @Test
    void calculateMostFrequentApiCalls_returnsEmptyList_whenNoValidApiLogs() {
        var allInvalidDtos = List.of(INVALID_API_LOG_DTO);
        var validRows =
            fixture.getValidRows(allInvalidDtos).stream()
                .map(ApiLog::fromDto)
                .toList();

        assertTrue(validRows.isEmpty());

        var result = fixture.calculateMostFrequentApiCalls(validRows, testResultsLimit);

        assertTrue(result.isEmpty());
    }
}
