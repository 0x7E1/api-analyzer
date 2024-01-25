package io.skai.platform.apianalyzer.util;

import io.skai.platform.apianalyzer.BaseApiAnalyzerTest;
import io.skai.platform.apianalyzer.model.AnalyticsResult;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvHelperTest extends BaseApiAnalyzerTest {

    @Test
    void checkCsvFormat_returnsTrue_whenFileExtensionIsCsv() {
        assertTrue(CsvHelper.checkCsvFormat(VALID_CONTENT_TYPE));
    }

    @Test
    void checkCsvFormat_returnsFalse_whenFileExtensionIsNotCsv() {
        assertFalse(CsvHelper.checkCsvFormat(INVALID_CONTENT_TYPE));
    }

    @Test
    void checkCsvFormat_throwsNullPointerException_whenFileExtensionIsNull() {
        assertThrows(NullPointerException.class, () -> CsvHelper.checkCsvFormat(null));
    }

    @Test
    void convertToCsv_returnsCsvString_whenAnalyticsResultsNotEmpty() {
        var analyticsResults = generateAnalyticsResults();
        var result = CsvHelper.convertToCsv(analyticsResults);

        assertFalse(result.isBlank());
        assertTrue(result.contains(CsvHelper.CSV_RESULTS_HEADER));
        assertTrue(result.contains(VALID_ENDPOINT));
        assertTrue(result.contains(VALID_HTTP_METHOD));
        assertTrue(result.contains(VALID_API_LOG_INVOCATIONS.toString()));
    }

    @Test
    void convertToCsv_returnsEmptyString_whenEmptyAnalyticsResults() {
        List<AnalyticsResult> list = List.of();
        var result = CsvHelper.convertToCsv(list);

        assertTrue(StringUtils.isBlank(result));
    }
}
