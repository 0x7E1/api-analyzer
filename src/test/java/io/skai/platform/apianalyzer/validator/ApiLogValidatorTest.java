package io.skai.platform.apianalyzer.validator;

import io.skai.platform.apianalyzer.BaseApiAnalyzerTest;
import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.impl.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiLogValidatorTest extends BaseApiAnalyzerTest {
    ApiLogValidator apiLogValidator;

    @Test
    void ipAddressValidator_returnsTrue() {
        apiLogValidator = new IpAddressValidator();
        var dto = ApiLogDto.builder()
            .ipAddress(VALID_IP_ADDRESS)
            .build();

        assertTrue(apiLogValidator.isValid(dto));
    }

    @Test
    void ipAddressValidator_returnsFalse() {
        apiLogValidator = new IpAddressValidator();
        var dto = ApiLogDto.builder()
            .ipAddress(INVALID_IP_ADDRESS)
            .build();

        assertFalse(apiLogValidator.isValid(dto));
    }

    @Test
    void timestampValidator_returnsTrue() {
        apiLogValidator = new TimestampValidator();
        var dto = ApiLogDto.builder()
            .timestamp(VALID_TIMESTAMP)
            .build();

        assertTrue(apiLogValidator.isValid(dto));
    }

    @Test
    void timestampValidator_returnsFalse() {
        apiLogValidator = new TimestampValidator();
        var dto = ApiLogDto.builder()
            .timestamp(INVALID_TIMESTAMP)
            .build();

        assertFalse(apiLogValidator.isValid(dto));
    }

    @Test
    void endpointValidator_returnsTrue() {
        apiLogValidator = new EndpointPathValidator();
        var dto = ApiLogDto.builder()
            .endpoint(VALID_ENDPOINT)
            .build();

        assertTrue(apiLogValidator.isValid(dto));
    }

    @Test
    void endpointValidator_returnsFalse() {
        apiLogValidator = new EndpointPathValidator();
        var dto = ApiLogDto.builder()
            .endpoint(INVALID_ENDPOINT)
            .build();

        assertFalse(apiLogValidator.isValid(dto));
    }

    @Test
    void httpMethodValidator_returnsTrue() {
        apiLogValidator = new HttpMethodValidator();
        var dto = ApiLogDto.builder()
            .method(VALID_HTTP_METHOD)
            .build();

        assertTrue(apiLogValidator.isValid(dto));
    }

    @Test
    void httpMethodValidator_returnsFalse() {
        apiLogValidator = new HttpMethodValidator();
        var dto = ApiLogDto.builder()
            .method(INVALID_HTTP_METHOD)
            .build();

        assertFalse(apiLogValidator.isValid(dto));
    }

    @Test
    void httpStatusValidator_returnsTrue() {
        apiLogValidator = new HttpStatusValidator();
        var dto = ApiLogDto.builder()
            .status(VALID_HTTP_STATUS)
            .build();

        assertTrue(apiLogValidator.isValid(dto));
    }

    @Test
    void httpStatusValidator_returnsFalse() {
        apiLogValidator = new HttpStatusValidator();
        var dto = ApiLogDto.builder()
            .status(INVALID_HTTP_STATUS)
            .build();

        assertFalse(apiLogValidator.isValid(dto));
    }
}
