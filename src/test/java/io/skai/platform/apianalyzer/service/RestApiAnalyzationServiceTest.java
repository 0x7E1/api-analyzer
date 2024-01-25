package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.BaseApiAnalyzerTest;
import io.skai.platform.apianalyzer.exception.ResourceNotFoundException;
import io.skai.platform.apianalyzer.model.ApiLog;
import io.skai.platform.apianalyzer.service.impl.ConsoleAnalyzationService;
import io.skai.platform.apianalyzer.service.impl.RestApiAnalyzationService;
import io.skai.platform.apianalyzer.service.impl.WebCsvFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class RestApiAnalyzationServiceTest extends BaseApiAnalyzerTest {
    WebCsvFileLoader fileLoaderMock;
    ConsoleAnalyzationService consoleAnalyzationServiceMock;
    MultipartFile csvFileMock;

    RestApiAnalyzationService fixture;

    @BeforeEach
    void setUp() throws IOException {
        fileLoaderMock = mock(WebCsvFileLoader.class);
        consoleAnalyzationServiceMock = mock(ConsoleAnalyzationService.class);
        csvFileMock = mock(MultipartFile.class);

        when(csvFileMock.getBytes()).thenReturn(INPUT_DATA_SAMPLE.getBytes());
        when(fileLoaderMock.loadApiLogs()).thenReturn(generateApiLogDtos());

        fixture = new RestApiAnalyzationService(fileLoaderMock, consoleAnalyzationServiceMock);
        fixture.resultsLimit = testResultsLimit;
    }

    @Test
    void generateResults_returnsAnalyticsResult() {
        var validApiLogs = fixture.getValidRows(generateApiLogDtos()).stream()
            .map(ApiLog::fromDto)
            .toList();

        var result = fixture.generateResults(validApiLogs);

        assertFalse(result.isEmpty());
        assertEquals(VALID_API_LOG_DTO.endpoint(), result.get(0).getEndpoint());
        assertEquals(VALID_API_LOG_DTO.method(), result.get(0).getHttpMethod().name());
        assertEquals(VALID_API_LOG_INVOCATIONS, result.get(0).getInvocationsCount());
        assertEquals(VALID_API_LOG_DTO_2.endpoint(), result.get(1).getEndpoint());
        assertEquals(VALID_API_LOG_DTO_2.method(), result.get(1).getHttpMethod().name());
        assertEquals(1, result.get(1).getInvocationsCount());
    }

    @Test
    void getResults_returnsAnalyticsResult() {
        var existedToken = fixture.addResource(csvFileMock);
        var result = fixture.getResults(existedToken);

        assertFalse(result.isEmpty());
        assertEquals(VALID_ENDPOINT, result.get(0).getEndpoint());
        assertEquals(VALID_HTTP_METHOD, result.get(0).getHttpMethod().name());
        assertEquals(VALID_API_LOG_INVOCATIONS, result.get(0).getInvocationsCount());
    }

    @Test
    void getResults_returnsNull_whenCanNotLoadInputData() {
        when(fileLoaderMock.loadApiLogs()).thenReturn(null);

        var existedToken = fixture.addResource(csvFileMock);
        var result = fixture.getResults(existedToken);

        assertNull(result);
    }

    @Test
    void getResults_throwsResourceNotFoundException_whenSuchTokenDoesNotExist() {
        var notExistedToken = UUID.randomUUID();
        assertThrows(ResourceNotFoundException.class, () -> fixture.getResults(notExistedToken));
    }
}
