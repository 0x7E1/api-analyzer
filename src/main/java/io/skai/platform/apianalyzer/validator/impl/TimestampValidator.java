package io.skai.platform.apianalyzer.validator.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampValidator implements ApiLogValidator {
    private static final Logger LOG = LoggerFactory.getLogger(TimestampValidator.class);
    public static final String DATETIME_PATTERN = "dd/MM/yyyy:HH:mm:ssZ";

    @Override
    public boolean isValid(ApiLogDto dto) {
        var timestamp = dto.timestamp();
        try {
            LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern(DATETIME_PATTERN));
            return true;
        } catch (Exception e) {
            LOG.error("Invalid date-time format of \"{}\". Valid schema: dd/MM/yyyy:HH:mm:ssZ", timestamp);
            return false;
        }
    }
}
