package io.skai.platform.apianalyzer.service;

import io.skai.platform.apianalyzer.dto.ApiLogDto;
import io.skai.platform.apianalyzer.validator.ApiLogValidator;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AnalyzerService {
    private final FileReader fileReader;
    private final ApiLogValidator validator;
    private final OutputPrinter printer;

    public AnalyzerService(FileReader fileReader, ApiLogValidator validator, OutputPrinter printer) {
        this.fileReader = fileReader;
        this.validator = validator;
        this.printer = printer;
    }

    @PostConstruct
    public void init() throws IOException {
        var dto = new ApiLogDto(
            "192.168.0.1",
            "28/07/2006:10:25:04+0300",
            "POST",
            "/save",
            "200"
        );
//        System.out.printf("%nValidation result: %s %n%n", validator.isValid(dto));

        fileReader.loadData();
        validator.isValid(dto);
        printer.generateAnalytics();
    }
}
