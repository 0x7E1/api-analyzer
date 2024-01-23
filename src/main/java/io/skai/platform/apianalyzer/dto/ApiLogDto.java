package io.skai.platform.apianalyzer.dto;

import lombok.Builder;

@Builder
public record ApiLogDto(
    String ipAddress,
    String timestamp,
    String method,
    String endpoint,
    String status
) {
}
