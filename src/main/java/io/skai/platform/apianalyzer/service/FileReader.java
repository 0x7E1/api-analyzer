package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReader {
    private static final String INPUT_FILE_PATH = "classpath:statistic.csv";
    private static final String INPUT_DATA_DELIMITER = ";";

    private final ResourceLoader resourceLoader;

    public FileReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<ApiLogDto> loadData() throws IOException {
        List<ApiLogDto> apiLogDtos;
        var resource = resourceLoader.getResource(INPUT_FILE_PATH);
        var reader = new InputStreamReader(resource.getInputStream());

        try (var br = new BufferedReader(reader)) {
            String line;
            apiLogDtos = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] rowValues = line.split(INPUT_DATA_DELIMITER);
                var apiLog = new ApiLogDto(
                    rowValues[0],
                    rowValues[1],
                    rowValues[2],
                    rowValues[3],
                    rowValues[4]
                );
                apiLogDtos.add(apiLog);
            }
        } catch (Exception e) {
            throw new IOException("Error occurred while reading the input file", e);
        }

        return apiLogDtos;
    }
}
