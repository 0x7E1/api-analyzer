package io.skai.platform.apianalyzer.controller;

import io.skai.platform.apianalyzer.dto.ResponseDto;
import io.skai.platform.apianalyzer.service.RestApiService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api-analyzer")
public class AnalyzerController {
    private final RestApiService apiService;

    public AnalyzerController(RestApiService apiService) {
        this.apiService = apiService;
    }

    @Operation(
        summary = "Upload CSV file with input data",
        description = "Returns a token which is required to get results for this input data file."
    )
    @PostMapping(
        path = "/upload-data",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDto> uploadData(@RequestPart("file") MultipartFile csvFile) {
        return apiService.uploadInputData(csvFile);
    }

    @Operation(summary = "Download CSV file with the API analytics")
    @GetMapping("/results")
    public ResponseEntity<byte[]> results(@RequestParam("token") String analyzationId) {
        return apiService.results(analyzationId);
    }
}
