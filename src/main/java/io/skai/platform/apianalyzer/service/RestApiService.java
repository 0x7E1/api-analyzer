package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.dto.ResponseDto;
import io.skai.platform.apianalyzer.service.impl.RestApiAnalyzationService;
import io.skai.platform.apianalyzer.util.CsvHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
public class RestApiService {
    private final RestApiAnalyzationService restAnalyzationService;

    RestApiService(RestApiAnalyzationService restApiAnalyzationService) {
        restAnalyzationService = restApiAnalyzationService;
    }

    public ResponseEntity<ResponseDto> uploadInputData(MultipartFile csvFile) {
        if (!CsvHelper.checkCsvFormat(Objects.requireNonNull(csvFile.getContentType())) || csvFile.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.builder()
                    .message("Uploaded file is invalid! Use non-empty CSV file.")
                    .build());
        }

        var token = UUID.randomUUID();
        restAnalyzationService.addResource(token, csvFile);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ResponseDto.builder()
                .message("CSV file uploaded successfully.")
                .analyzationId(token)
                .build());
    }

    public ResponseEntity<byte[]> results(String analyzationId) {
        var results = restAnalyzationService.getResults(UUID.fromString(analyzationId));
        if (!results.isEmpty()) {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "api_analytics_results.csv");

            return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(CsvHelper.convertToCsv(results));
        }

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .build();
    }
}
