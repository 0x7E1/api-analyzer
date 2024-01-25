package io.skai.platform.apianalyzer.validator.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

public class HttpMethodValidator implements ApiLogValidator {
    private static final Logger LOG = LoggerFactory.getLogger(HttpMethodValidator.class);

    @Override
    public boolean isValid(ApiLogDto dto) {
        var httpMethod = dto.method();
        if (!Arrays.asList(HttpMethod.values()).contains(HttpMethod.valueOf(httpMethod))) {
            LOG.error("Unknown HTTP method \"{}\"", httpMethod);
            return false;
        }

        return true;
    }
}
