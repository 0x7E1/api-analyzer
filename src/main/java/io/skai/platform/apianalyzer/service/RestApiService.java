package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.dto.ResponseDto;
import io.skai.platform.apianalyzer.service.impl.RestApiAnalyzationService;
import io.skai.platform.apianalyzer.util.CsvHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Service
public class RestApiService {
    private static final Logger LOG = LoggerFactory.getLogger(RestApiService.class);
    private static final String RESULT_FILENAME = "analytics_result.csv";

    private final RestApiAnalyzationService restAnalyzationService;

    RestApiService(RestApiAnalyzationService restApiAnalyzationService) {
        restAnalyzationService = restApiAnalyzationService;
    }

    public ResponseEntity<ResponseDto> uploadInputData(MultipartFile csvFile) {
        if (!CsvHelper.checkCsvFormat(Objects.requireNonNull(csvFile.getContentType())) || csvFile.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.builder()
                    .message("Uploaded file is invalid!")
                    .build());
        }

        var token = restAnalyzationService.addResource(csvFile);
        LOG.info("New resource file successfully loaded. Resource token = {}", token);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.builder()
                .message("CSV file uploaded successfully")
                .token(token)
                .build());
    }

    public ResponseEntity<byte[]> results(String token) {
        var results = restAnalyzationService.getResults(UUID.fromString(token));
        if (results == null) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
        }

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", RESULT_FILENAME);

        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(headers)
            .body(CsvHelper.convertToCsv(results).getBytes(StandardCharsets.UTF_8));
    }
}
