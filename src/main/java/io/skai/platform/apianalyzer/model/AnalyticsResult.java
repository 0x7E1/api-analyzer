package io.skai.platform.apianalyzer.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
@Builder
public class AnalyticsResult {
    String endpoint;
    HttpMethod httpMethod;
    int invocationsCount;
}
