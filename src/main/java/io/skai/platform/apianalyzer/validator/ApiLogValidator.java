package io.skai.platform.apianalyzer.validator;

import io.skai.platform.apianalyzer.dto.ApiLogDto;

public interface ApiLogValidator {
    boolean isValid(ApiLogDto dto);
}
