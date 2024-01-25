package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.BaseApiAnalyzerTest;
import io.skai.platform.apianalyzer.service.impl.LocalCsvFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalCsvFileLoaderTest extends BaseApiAnalyzerTest {
    Resource resourceMock;
    ResourceLoader resourceLoaderMock;

    LocalCsvFileLoader fixture;

    @BeforeEach
    void setUp() throws IOException {
        resourceMock = mock(Resource.class);
        resourceLoaderMock = mock(ResourceLoader.class);
        var is = new ByteArrayInputStream(INPUT_DATA_SAMPLE.getBytes());

        when(resourceMock.getInputStream()).thenReturn(is);
        when(resourceLoaderMock.getResource(anyString())).thenReturn(resourceMock);

        fixture = new LocalCsvFileLoader(resourceLoaderMock);
    }

    @Test
    void loadApiLogs_returnsApiLogDtos() {
        var result = fixture.loadApiLogs();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void loadApiLogs_returnsNull_whenFileNotFound() throws IOException {
        when(resourceMock.getInputStream()).thenThrow(NullPointerException.class);

        assertNull(fixture.loadApiLogs());
    }
}
