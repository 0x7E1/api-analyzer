package io.skai.platform.apianalyzer.entity;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@AllArgsConstructor
public class ApiLog {
    public String ipAddress;
    public LocalDateTime timestamp;
    public HttpMethod httpMethod;
    public String endpoint;
    public HttpStatus status;

    public ApiLog(HttpMethod httpMethod, String endpoint) {
        this.httpMethod = httpMethod;
        this.endpoint = endpoint;
    }

    public ApiLog(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static ApiLog fromDto(ApiLogDto dto) {
        return new ApiLog(
            dto.ipAddress,
            LocalDateTime.parse(dto.timestamp, DateTimeFormatter.ofPattern(ApiLogValidator.DATETIME_PATTERN)),
            HttpMethod.valueOf(dto.method),
            dto.endpoint,
            HttpStatus.valueOf(Integer.parseInt(dto.status))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiLog apiLog = (ApiLog) o;
        return Objects.equals(ipAddress, apiLog.ipAddress)
            && Objects.equals(timestamp, apiLog.timestamp)
            && Objects.equals(httpMethod, apiLog.httpMethod)
            && Objects.equals(endpoint, apiLog.endpoint)
            && status == apiLog.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, timestamp, httpMethod, endpoint, status);
    }
}
