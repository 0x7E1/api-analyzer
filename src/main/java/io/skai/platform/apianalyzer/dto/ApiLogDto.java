package io.skai.platform.apianalyzer.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiLogDto {
    public String ipAddress;
    public String timestamp;
    public String method;
    public String endpoint;
    public String status;
}
