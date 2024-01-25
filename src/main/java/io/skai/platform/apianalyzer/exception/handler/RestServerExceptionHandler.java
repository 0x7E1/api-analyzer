package io.skai.platform.apianalyzer.exception.handler;

import io.skai.platform.apianalyzer.dto.ResponseDto;
import io.skai.platform.apianalyzer.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class RestServerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseDto> handleFileReadingError() {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ResponseDto.builder()
                .message("Provided CSV file is invalid. Check file's format")
                .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> handleResourceNotFoundError() {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ResponseDto.builder()
                .message("Resource not found or invalid token")
                .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handleGeneralServerError() {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseDto.builder()
                .message("Execution failed due to server error. See the logs for more details")
                .build());
    }
}
