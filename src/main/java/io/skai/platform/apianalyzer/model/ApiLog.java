package io.skai.platform.apianalyzer.model;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.impl.TimestampValidator;
import lombok.Builder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
public record ApiLog(
    String ipAddress,
    LocalDateTime timestamp,
    HttpMethod httpMethod,
    String endpoint,
    HttpStatus status
) {
    public static ApiLog fromDto(ApiLogDto dto) {
        return new ApiLog(
            dto.ipAddress(),
            LocalDateTime.parse(dto.timestamp(), DateTimeFormatter.ofPattern(TimestampValidator.DATETIME_PATTERN)),
            HttpMethod.valueOf(dto.method()),
            dto.endpoint(),
            HttpStatus.valueOf(Integer.parseInt(dto.status()))
        );
    }
}
