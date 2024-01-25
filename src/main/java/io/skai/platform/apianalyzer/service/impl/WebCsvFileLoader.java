package io.skai.platform.apianalyzer.service.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.service.CsvDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class WebCsvFileLoader implements CsvDataLoader {
    private static final Logger LOG = LoggerFactory.getLogger(WebCsvFileLoader.class);

    private InputStream inputStream;

    @Override
    public List<ApiLogDto> loadApiLogs() {
        try {
            return fromInputStream(inputStream);
        } catch (Exception e) {
            LOG.error("Error occurred while reading the input file", e);
            return null;
        }
    }

    public void setResource(InputStream is) {
        inputStream = is;
    }
}
