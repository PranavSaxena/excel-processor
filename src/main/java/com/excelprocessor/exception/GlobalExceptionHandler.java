package com.excelprocessor.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileException(FileProcessingException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
