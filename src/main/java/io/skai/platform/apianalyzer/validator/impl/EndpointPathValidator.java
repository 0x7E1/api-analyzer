package io.skai.platform.apianalyzer.validator.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class EndpointPathValidator implements ApiLogValidator {
    private static final Logger LOG = LoggerFactory.getLogger(EndpointPathValidator.class);
    private static final String ENDPOINT_PATH_PATTERN = "^/\\w+(?:[.:~-]\\w+)*(?:/\\w+(?:[.:~-]\\w+)*)*$";

    @Override
    public boolean isValid(ApiLogDto dto) {
        var endpoint = dto.endpoint();
        if (endpoint.equals("/")) { return true; }

        var pattern = Pattern.compile(ENDPOINT_PATH_PATTERN);
        var matcher = pattern.matcher(endpoint);

        if (matcher.matches()) {
            return true;
        } else {
            LOG.error("Invalid URL path format of \"{}\". Ensure that path does not end with the slash", endpoint);
            return false;
        }
    }
}
