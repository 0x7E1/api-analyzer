package io.skai.platform.apianalyzer.util;

import io.skai.platform.apianalyzer.model.AnalyticsResult;

import java.util.List;

public class CsvHelper {
    private static final String CSV_FORMAT = "text/csv";
    public static final String CSV_RESULTS_HEADER = "Endpoint;HTTP Method;Calls\n";

    public static boolean checkCsvFormat(String fileExtension) {
        return fileExtension.equals(CSV_FORMAT);
    }

    public static String convertToCsv(List<AnalyticsResult> results) {
        var csvContent = new StringBuilder();
        if (!results.isEmpty()) {
            csvContent.append(CSV_RESULTS_HEADER);

            for (var result : results) {
                csvContent
                    .append(result.getEndpoint()).append(";")
                    .append(result.getHttpMethod()).append(";")
                    .append(result.getInvocationsCount()).append("\n");
            }
        }

        return csvContent.toString();
    }
}
