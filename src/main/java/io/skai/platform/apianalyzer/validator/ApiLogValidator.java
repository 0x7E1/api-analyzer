package io.skai.platform.apianalyzer.validator;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

@Component
public class ApiLogValidator {
    private static final String IPV4_PATTERN =
        "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
        "(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){3}$";
    private static final String ENDPOINT_PATH_PATTERN = "^/\\w+(?:[.:~-]\\w+)*(?:/\\w+(?:[.:~-]\\w+)*)*$";
    private static final String DATETIME_PATTERN = "dd/MM/yyyy:HH:mm:ssZ";
    private static final Logger log = LoggerFactory.getLogger(ApiLogValidator.class);

    public boolean isValid(ApiLogDto dto) {
        log.debug("Validating " + dto);

        return validateIp(dto.ipAddress)
            && validateTimestamp(dto.timestamp)
            && validateEndpoint(dto.endpoint)
            && validateHttpMethod(dto.method)
            && validateHttpStatus(dto.status);
    }

    private boolean validateIp(String ipAddress) {
        var pattern = Pattern.compile(IPV4_PATTERN);
        var matcher = pattern.matcher(ipAddress);

        if (!matcher.matches()) {
            log.warn("Invalid IP address format!");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateTimestamp(String timestamp) {
        try {
            LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern(DATETIME_PATTERN));
            return true;
        } catch (Exception e) {
            log.warn("Invalid date-time format! Valid schema: DD/MM/YYYY:HH:MM:SS-GMT");
            return false;
        }
    }

    private boolean validateEndpoint(String endpoint) {
        var pattern = Pattern.compile(ENDPOINT_PATH_PATTERN);
        var matcher = pattern.matcher(endpoint);

        if (!matcher.matches()) {
            log.warn("Invalid URL path format! Ensure that path does not end with the slash.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateHttpMethod(String method) {
        try {
            var httpMethod = HttpMethod.valueOf(method.toUpperCase());
            return Arrays.asList(HttpMethod.values()).contains(httpMethod);
        } catch (Exception e) {
            log.warn("Unknown HTTP method!");
            return false;
        }
    }

    private boolean validateHttpStatus(String status) {
        try {
            var statusCode = HttpStatus.valueOf(Integer.parseInt(status));
            return Arrays.asList(HttpStatus.values()).contains(statusCode);
        } catch (Exception e) {
            log.warn("Unknown HTTP status code!");
            return false;
        }
    }
}
