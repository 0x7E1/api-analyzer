package io.skai.platform.apianalyzer;

import com.google.common.collect.Lists;
import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.model.AnalyticsResult;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
public class BaseApiAnalyzerTest {
    protected static final String VALID_IP_ADDRESS = "192.168.0.1";
    protected static final String VALID_TIMESTAMP = "28/07/2006:10:22:04-0300";
    protected static final String VALID_HTTP_METHOD = "POST";
    protected static final String VALID_ENDPOINT = "/user/info";
    protected static final String VALID_HTTP_STATUS = "200";

    protected static final ApiLogDto VALID_API_LOG_DTO =
        ApiLogDto.builder()
            .ipAddress(VALID_IP_ADDRESS)
            .timestamp(VALID_TIMESTAMP)
            .method(VALID_HTTP_METHOD)
            .endpoint(VALID_ENDPOINT)
            .status(VALID_HTTP_STATUS)
            .build();
    protected static final ApiLogDto VALID_API_LOG_DTO_2 =
        ApiLogDto.builder()
            .ipAddress("127.0.0.1")
            .timestamp("25/01/2024:10:59:04+0200")
            .method("GET")
            .endpoint("/result")
            .status("404")
            .build();

    protected static final String INVALID_IP_ADDRESS = "0.0.0.0";
    protected static final String INVALID_TIMESTAMP = "2011-12-03T10:15:30+01:00";
    protected static final String INVALID_HTTP_METHOD = "WRONG";
    protected static final String INVALID_ENDPOINT = "invalid/url.";
    protected static final String INVALID_HTTP_STATUS = "999";

    protected static final ApiLogDto INVALID_API_LOG_DTO =
        ApiLogDto.builder()
            .ipAddress(INVALID_IP_ADDRESS)
            .timestamp(INVALID_TIMESTAMP)
            .method(INVALID_HTTP_METHOD)
            .endpoint(INVALID_ENDPOINT)
            .status(INVALID_HTTP_STATUS)
            .build();

    protected static final String VALID_CONTENT_TYPE = "text/csv";
    protected static final String INVALID_CONTENT_TYPE = "application/json";

    protected static final Integer VALID_API_DTOS_COUNT = 3;
    protected static final Integer VALID_API_LOG_INVOCATIONS = 2;

    protected static final String INPUT_DATA_SAMPLE =
        String.format("%s;%s;%s;%s;%s", VALID_IP_ADDRESS, VALID_TIMESTAMP, VALID_HTTP_METHOD, VALID_ENDPOINT, VALID_HTTP_STATUS);

    @Value("${analyzer.results.limit}")
    protected Long testResultsLimit;

    // Update VALID_API_DTOS_COUNT and VALID_API_LOG_INVOCATIONS constants when adding new valid elements
    protected List<ApiLogDto> generateApiLogDtos() {
        return Lists.newArrayList(
            VALID_API_LOG_DTO,
            VALID_API_LOG_DTO,
            INVALID_API_LOG_DTO,
            INVALID_API_LOG_DTO,
            VALID_API_LOG_DTO_2
        );
    }

    protected List<AnalyticsResult> generateAnalyticsResults() {
        return List.of(
            AnalyticsResult.builder()
                .endpoint(VALID_ENDPOINT)
                .httpMethod(HttpMethod.valueOf(VALID_HTTP_METHOD))
                .invocationsCount(VALID_API_LOG_INVOCATIONS)
                .build()
        );
    }
}
