package io.skai.platform.apianalyzer.util;

import io.skai.platform.apianalyzer.model.AnalyticsResult;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvHelper {
    private static final String CSV_FORMAT = "text/csv";
    private static final String CSV_RESULTS_HEADER = "Endpoint;HTTP Method;Calls\n";

    public static boolean checkCsvFormat(String fileExtension) {
        return fileExtension.equals(CSV_FORMAT);
    }

    public static byte[] convertToCsv(List<AnalyticsResult> results) {
        var csvContent = new StringBuilder();
        csvContent.append(CSV_RESULTS_HEADER);

        for (var result : results) {
            csvContent
                .append(result.getEndpoint()).append(";")
                .append(result.getHttpMethod()).append(";")
                .append(result.getInvocationsCount()).append("\n");
        }

        return csvContent.toString().getBytes(StandardCharsets.UTF_8);
    }
}
