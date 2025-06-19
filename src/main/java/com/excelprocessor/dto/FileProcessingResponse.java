package com.excelprocessor.dto;

public class FileProcessingResponse {
    private String message;
    private String outputFile;

    public FileProcessingResponse(String message, String outputFile) {
        this.message = message;
        this.outputFile = outputFile;
    }

    public String getMessage() {
    	return message; 
    }
    
    public String getOutputFile() { 
    	return outputFile; 
    }
}
