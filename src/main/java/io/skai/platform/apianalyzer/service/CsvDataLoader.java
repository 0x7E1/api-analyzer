package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.dto.ApiLogDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public interface CsvDataLoader {
    default List<ApiLogDto> fromInputStream(InputStream is) throws IOException {
        List<ApiLogDto> apiLogDtos = new ArrayList<>();
        String line;
        var br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        while ((line = br.readLine()) != null) {
            String[] rowValues = line.split(";");
            var apiLog = ApiLogDto.builder()
                .ipAddress(rowValues[0])
                .timestamp(rowValues[1])
                .method(rowValues[2])
                .endpoint(rowValues[3])
                .status(rowValues[4])
                .build();
            apiLogDtos.add(apiLog);
        }

        return apiLogDtos;
    }

    List<ApiLogDto> loadApiLogs();
}
