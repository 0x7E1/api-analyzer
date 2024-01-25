package io.skai.platform.apianalyzer.service.impl;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.service.CsvDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalCsvFileLoader implements CsvDataLoader {
    private static final Logger LOG = LoggerFactory.getLogger(LocalCsvFileLoader.class);
    private static final String INPUT_FILE_PATH = "classpath:statistic.csv";

    private final ResourceLoader resourceLoader;

    public LocalCsvFileLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public List<ApiLogDto> loadApiLogs() {
        var resource = resourceLoader.getResource(INPUT_FILE_PATH);

        try (var is = resource.getInputStream()) {
            return fromInputStream(is);
        } catch (Exception e) {
            LOG.error("Error occurred while reading 'statistic.csv' file", e);
            return null;
        }
    }
}
