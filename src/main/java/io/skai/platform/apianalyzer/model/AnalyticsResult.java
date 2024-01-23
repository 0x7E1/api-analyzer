package io.skai.platform.apianalyzer.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalyticsResult {
    String endpoint;
    String httpMethod;
    int invocationsCount;

    public static AnalyticsResult fromApiLog(ApiLog apiLog, int invocationsCount) {
        return new AnalyticsResult(
            apiLog.endpoint(),
            apiLog.httpMethod().name(),
            invocationsCount
        );
    }
}
