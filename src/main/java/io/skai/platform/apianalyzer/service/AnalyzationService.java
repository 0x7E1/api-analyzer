package io.skai.platform.apianalyzer.service;

import com.google.common.collect.Lists;
import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.model.AnalyticsResult;
import io.skai.platform.apianalyzer.model.ApiLog;
import io.skai.platform.apianalyzer.model.enums.ValidationType;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import io.skai.platform.apianalyzer.validator.impl.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    default List<Map.Entry<ApiLog, Long>> calculateMostFrequentApiCalls(List<ApiLog> rows, long resultsLimit) {
        return rows.stream()
            .map(it -> ApiLog.builder()
                .ipAddress(it.ipAddress())
                .timestamp(it.timestamp())
                .httpMethod(it.httpMethod())
                .endpoint(it.endpoint())
                .httpMethod(it.httpMethod())
                .build())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(resultsLimit)
            .toList();
    }

    default List<ApiLogDto> getValidRows(List<ApiLogDto> inputData) {
        ApiLogValidator validator;
        ArrayList<ApiLogDto> invalidRows = Lists.newArrayList();
        for (var validationType : ValidationType.values()) {
            validator = setValidationStrategy(validationType);

            for (ApiLogDto dto : inputData) {
                if (!validator.isValid(dto)) {
                    invalidRows.add(dto);
                }
            }
        }

        return inputData.stream()
            .filter(it -> !invalidRows.contains(it))
            .toList();
    }

    List<AnalyticsResult> generateResults(List<ApiLog> rows);
    void generateStatistics(List<ApiLog> rows);
    void generateCounters(int totalRows, int validRows, long executionTime);
}
