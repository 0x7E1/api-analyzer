package io.skai.platform.apianalyzer.entity;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ApiLog {
    private String ipAddress;
    private LocalDateTime timestamp;
    private HttpMethod httpMethod;
    private String endpoint;
    private HttpStatus status;

    public static ApiLog fromDto(ApiLogDto dto) {
        return new ApiLog(
            dto.ipAddress,
            LocalDateTime.parse(dto.timestamp),
            HttpMethod.valueOf(dto.method),
            dto.endpoint,
            HttpStatus.valueOf(dto.status)
        );
    }
}
