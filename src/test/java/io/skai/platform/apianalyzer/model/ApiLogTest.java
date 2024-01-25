package io.skai.platform.apianalyzer.model;

import io.skai.platform.apianalyzer.BaseApiAnalyzerTest;
import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.impl.TimestampValidator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class ApiLogTest extends BaseApiAnalyzerTest {

    @Test
    void fromDto_shouldCorrectlyConvertToApiLog() {
        var result = ApiLog.fromDto(VALID_API_LOG_DTO);

        assertNotNull(result);
        assertInstanceOf(ApiLog.class, result);

        assertEquals(VALID_IP_ADDRESS, result.ipAddress());
        assertEquals(LocalDateTime.parse(VALID_TIMESTAMP, DateTimeFormatter.ofPattern(TimestampValidator.DATETIME_PATTERN)), result.timestamp());
        assertEquals(HttpMethod.valueOf(VALID_HTTP_METHOD), result.httpMethod());
        assertEquals(VALID_ENDPOINT, result.endpoint());
        assertEquals(HttpStatus.valueOf(Integer.parseInt(VALID_HTTP_STATUS)), result.status());
    }

    @Test
    void fromDto_throwsIllegalArgumentException_whenMethodIsNull() {
        var dto = ApiLogDto.builder()
            .ipAddress(VALID_IP_ADDRESS)
            .timestamp(VALID_TIMESTAMP)
            .method(null)
            .endpoint(VALID_ENDPOINT)
            .status(VALID_HTTP_STATUS)
            .build();

        assertThrows(IllegalArgumentException.class, () -> ApiLog.fromDto(dto));
    }

    @Test
    void fromDto_throwsIllegalArgumentException_whenInvalidStatus() {
        var dto = ApiLogDto.builder()
            .ipAddress(VALID_IP_ADDRESS)
            .timestamp(VALID_TIMESTAMP)
            .method(VALID_HTTP_METHOD)
            .endpoint(VALID_ENDPOINT)
            .status(INVALID_HTTP_STATUS)
            .build();

        assertThrows(IllegalArgumentException.class, () -> ApiLog.fromDto(dto));
    }

    @Test
    void fromDto_throwsDateTimeParseException_whenTimestampWithWrongPattern() {
        var dto = ApiLogDto.builder()
            .ipAddress(VALID_IP_ADDRESS)
            .timestamp(INVALID_TIMESTAMP)
            .method(VALID_HTTP_METHOD)
            .endpoint(VALID_ENDPOINT)
            .status(VALID_HTTP_STATUS)
            .build();

        assertThrows(DateTimeParseException.class, () -> ApiLog.fromDto(dto));
    }
}
