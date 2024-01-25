package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.BaseApiAnalyzerTest;
import io.skai.platform.apianalyzer.service.impl.RestApiAnalyzationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestApiServiceTest extends BaseApiAnalyzerTest {
    RestApiAnalyzationService restApiAnalyzationService;
    MultipartFile csvFileMock;

    RestApiService fixture;

    @BeforeEach
    void setUp() {
        restApiAnalyzationService = mock(RestApiAnalyzationService.class);
        csvFileMock = mock(MultipartFile.class);

        when(csvFileMock.getContentType()).thenReturn(VALID_CONTENT_TYPE);
        when(csvFileMock.isEmpty()).thenReturn(false);
        when(restApiAnalyzationService.addResource(any())).thenReturn(UUID.randomUUID());

        fixture = new RestApiService(restApiAnalyzationService);
    }

    @Test
    void uploadInputData_returnsOkStatusWithMessageAndToken() {
        var expectedMessage = "CSV file uploaded successfully";
        var result = fixture.uploadInputData(csvFileMock);

        assertNotNull(result.getBody());
        assertNotNull(result.getBody().token);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedMessage, result.getBody().message);
    }

    @Test
    void uploadInputData_returnsBadRequestStatusWithMessage_whenFileContentTypeIsNotCsv() {
        when(csvFileMock.getContentType()).thenReturn(INVALID_CONTENT_TYPE);

        var expectedMessage = "Uploaded file is invalid!";
        var result = fixture.uploadInputData(csvFileMock);

        assertNotNull(result.getBody());
        assertNull(result.getBody().token);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedMessage, result.getBody().message);
    }

    @Test
    void uploadInputData_returnsBadRequestStatusWithMessage_whenCsvFileIsEmpty() {
        when(csvFileMock.isEmpty()).thenReturn(true);

        var expectedMessage = "Uploaded file is invalid!";
        var result = fixture.uploadInputData(csvFileMock);

        assertNotNull(result.getBody());
        assertNull(result.getBody().token);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedMessage, result.getBody().message);
    }

    @Test
    void results_returnsOkStatusWithCsvFile() {
        var analyticsResults = generateAnalyticsResults();
        when(restApiAnalyzationService.getResults(any())).thenReturn(analyticsResults);

        var result = fixture.results(UUID.randomUUID().toString());

        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, result.getHeaders().getContentType());
    }

    @Test
    void results_returnsNotFoundStatus_whenAnalyticsResultsIsNull() {
        when(restApiAnalyzationService.getResults(any())).thenReturn(null);

        var result = fixture.results(UUID.randomUUID().toString());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
