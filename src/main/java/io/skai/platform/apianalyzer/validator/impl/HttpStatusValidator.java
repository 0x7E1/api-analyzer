package io.skai.platform.apianalyzer.validator.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class HttpStatusValidator implements ApiLogValidator {
    private static final Logger LOG = LoggerFactory.getLogger(HttpStatusValidator.class);

    @Override
    public boolean isValid(ApiLogDto dto) {
        var httpStatus = dto.status();
        try {
            var code = HttpStatus.valueOf(Integer.parseInt(httpStatus));
            return Arrays.asList(HttpStatus.values()).contains(code);
        } catch (Exception e) {
            LOG.error("Unknown HTTP status code \"{}\"", httpStatus);
            return false;
        }
    }
}
