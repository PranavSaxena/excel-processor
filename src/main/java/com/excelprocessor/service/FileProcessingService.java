package com.excelprocessor.service;

import com.excelprocessor.exception.FileProcessingException;
import com.excelprocessor.util.RuleConverter;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileProcessingService {

    private static final String OUTPUT_FILE = "EWNworkstreamAutomationOutput.xlsx";

    public String processFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int requiredTasksCol = getColumnIndex(sheet, "Required Tasks");
            int ruleCol = createRulesColumn(sheet);

            processRows(sheet, requiredTasksCol, ruleCol);

            try (FileOutputStream fileOut = new FileOutputStream(OUTPUT_FILE)) {
                workbook.write(fileOut);
            }

            return OUTPUT_FILE;
        } catch (IOException e) {
            throw new FileProcessingException("Error processing file: " + e.getMessage());
        }
    }

    private int getColumnIndex(Sheet sheet, String columnName) {
        Row header = sheet.getRow(0);
        for (Cell cell : header) {
            if (columnName.equalsIgnoreCase(cell.getStringCellValue())) {
                return cell.getColumnIndex();
            }
        }
        throw new FileProcessingException(columnName + " column not found.");
    }

    private int createRulesColumn(Sheet sheet) {
        Row header = sheet.getRow(0);
        int lastCol = header.getLastCellNum();
        Cell cell = header.createCell(lastCol);
        cell.setCellValue("Rules");
        return lastCol;
    }

    private void processRows(Sheet sheet, int requiredTasksCol, int ruleCol) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row != null){
                Cell taskCell = row.getCell(requiredTasksCol);
                String task = (taskCell != null) ? taskCell.getStringCellValue() : "";
                String ruleJson = RuleConverter.convertToRule(task);
                row.createCell(ruleCol).setCellValue(ruleJson);
            }
        }
    }
}
