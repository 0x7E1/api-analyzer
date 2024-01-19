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
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ApiLogValidator {
    private static final Logger LOG = LoggerFactory.getLogger(ApiLogValidator.class);

    private static final String ENDPOINT_PATH_PATTERN = "^/\\w+(?:[.:~-]\\w+)*(?:/\\w+(?:[.:~-]\\w+)*)*$";
    private static final String IPV4_PATTERN =
        "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
        "(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){3}$";
    public static final String DATETIME_PATTERN = "dd/MM/yyyy:HH:mm:ssZ";

    public List<ApiLogDto> getValidRows(List<ApiLogDto> dtos) {
        return dtos.stream()
            .filter(this::isValid)
            .toList();
    }

    private boolean isValid(ApiLogDto dto) {
        LOG.debug("Validating {}", dto);

        return validateIp(dto.ipAddress)
            && validateTimestamp(dto.timestamp)
            && validateHttpMethod(dto.method)
            && validateEndpoint(dto.endpoint)
            && validateHttpStatus(dto.status);
    }

    private boolean validateIp(String ipAddress) {
        var pattern = Pattern.compile(IPV4_PATTERN);
        var matcher = pattern.matcher(ipAddress);

        if (!matcher.matches()) {
            LOG.error("Invalid IP address format of \"{}\"", ipAddress);
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
            LOG.error("Invalid date-time format of \"{}\". Valid schema: dd/MM/yyyy:HH:mm:ssZ", timestamp);
            return false;
        }
    }

    private boolean validateEndpoint(String endpoint) {
        if (endpoint.equals("/")) { return true; }

        var pattern = Pattern.compile(ENDPOINT_PATH_PATTERN);
        var matcher = pattern.matcher(endpoint);

        if (!matcher.matches()) {
            LOG.error("Invalid URL path format of \"{}\". Ensure that path does not end with the slash", endpoint);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateHttpMethod(String method) {
        if (!Arrays.asList(HttpMethod.values()).contains(HttpMethod.valueOf(method))) {
            LOG.error("Unknown HTTP method \"{}\"", method);
            return false;
        }

        return true;
    }

    private boolean validateHttpStatus(String status) {
        try {
            var statusCode = HttpStatus.valueOf(Integer.parseInt(status));
            return Arrays.asList(HttpStatus.values()).contains(statusCode);
        } catch (Exception e) {
            LOG.error("Unknown HTTP status code \"{}\"", status);
            return false;
        }
    }
}
