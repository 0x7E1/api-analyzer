package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.BaseApiAnalyzerTest;
import io.skai.platform.apianalyzer.service.impl.WebCsvFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class WebCsvFileLoaderTest extends BaseApiAnalyzerTest {
    WebCsvFileLoader webFileLoader;

    @BeforeEach
    void setUp() {
        webFileLoader = new WebCsvFileLoader();
    }

    @Test
    void loadApiLogs_returnsApiLogDtos() {
        webFileLoader.setResource(new ByteArrayInputStream(INPUT_DATA_SAMPLE.getBytes()));
        var result = webFileLoader.loadApiLogs();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void loadApiLogs_returnsNull_whenInputStreamNotInitialized() {
        assertNull(webFileLoader.loadApiLogs());
    }
}
