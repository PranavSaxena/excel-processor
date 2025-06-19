package com.excelprocessor.controller;

import com.excelprocessor.dto.FileProcessingResponse;
import com.excelprocessor.service.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/xls")
public class FileUploadController {

    @Autowired
    private FileProcessingService fileProcessingService;

    @PostMapping("/upload")
    public ResponseEntity<FileProcessingResponse> uploadFile(@RequestParam MultipartFile file) {
        String outputPath = fileProcessingService.processFile(file);
        return ResponseEntity.ok(new FileProcessingResponse("File processed successfully", outputPath));
    }
}
