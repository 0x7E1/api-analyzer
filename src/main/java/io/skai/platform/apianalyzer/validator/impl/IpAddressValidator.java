package io.skai.platform.apianalyzer.validator.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class IpAddressValidator implements ApiLogValidator {
    private static final Logger LOG = LoggerFactory.getLogger(IpAddressValidator.class);
    private static final String IPV4_PATTERN =
        "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
            "(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){3}$";

    @Override
    public boolean isValid(ApiLogDto dto) {
        var pattern = Pattern.compile(IPV4_PATTERN);
        var matcher = pattern.matcher(dto.ipAddress());

        if (matcher.matches()) {
            return true;
        } else {
            LOG.error("Invalid IP address format of \"{}\"", dto.ipAddress());
            return false;
        }
    }
}
