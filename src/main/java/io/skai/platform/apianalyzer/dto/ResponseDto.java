package io.skai.platform.apianalyzer.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public class ResponseDto {
    public String message;
    public UUID analyzationId;
}
