package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.model.AnalyticsResult;
import io.skai.platform.apianalyzer.model.ApiLog;
import io.skai.platform.apianalyzer.model.enums.ValidationType;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import io.skai.platform.apianalyzer.validator.impl.*;

import java.util.List;
import java.util.Map;

import static io.skai.platform.apianalyzer.model.enums.ValidationType.*;

public interface AnalyzationService {
    default ApiLogValidator setValidationStrategy(ValidationType type) {
        final Map<ValidationType, ApiLogValidator> validationStrategies =
            Map.of(
                IP_ADDRESS, new IpAddressValidator(),
                TIMESTAMP, new TimestampValidator(),
                HTTP_METHOD, new HttpMethodValidator(),
                HTTP_STATUS, new HttpStatusValidator(),
                ENDPOINT_PATH, new EndpointPathValidator()
            );

        return switch (type) {
            case IP_ADDRESS -> validationStrategies.get(IP_ADDRESS);
            case TIMESTAMP -> validationStrategies.get(TIMESTAMP);
            case HTTP_METHOD -> validationStrategies.get(HTTP_METHOD);
            case HTTP_STATUS -> validationStrategies.get(HTTP_STATUS);
            case ENDPOINT_PATH -> validationStrategies.get(ENDPOINT_PATH);
        };
    }

    List<AnalyticsResult> calculateResults(List<ApiLog> rows);
    void calculateStatistics(List<ApiLog> rows);
    void calculateCounters(int totalRows, int validRows, long executionTime);
}
