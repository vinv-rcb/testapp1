package com.nhalamphitrentroi.testapp1.service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhalamphitrentroi.testapp1.entity.DatabaseLog;
import com.nhalamphitrentroi.testapp1.repository.DatabaseLogRepository;

/**
 * Service for report generation and export
 */
@Service
public class ReportService {

    @Autowired
    private DatabaseLogRepository databaseLogRepository;

    /**
     * Export database logs to CSV file
     */
    public byte[] exportToCsv() throws IOException {
        List<DatabaseLog> logs = databaseLogRepository.findAll();
        
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Database Name,SQL,Execution Time,Execution Count,Created At\n");
        
        for (DatabaseLog log : logs) {
            csvContent.append(escapeCsvField(log.getDatabaseName())).append(",");
            csvContent.append(escapeCsvField(log.getSql())).append(",");
            csvContent.append(log.getExeTime()).append(",");
            csvContent.append(log.getExeCount()).append(",");
            csvContent.append(log.getCreatedAt()).append("\n");
        }
        
        return csvContent.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Export database logs to PDF file
     */
    public byte[] exportToPdf() throws IOException {
        List<DatabaseLog> logs = databaseLogRepository.findAll();
        
        StringBuilder pdfContent = new StringBuilder();
        pdfContent.append("BÁO CÁO TỔNG HỢP LOG DATABASE\n");
        pdfContent.append("================================\n\n");
        pdfContent.append("Tổng số bản ghi: ").append(logs.size()).append("\n\n");
        
        pdfContent.append("Chi tiết:\n");
        pdfContent.append("----------\n");
        
        for (DatabaseLog log : logs) {
            pdfContent.append("Database: ").append(log.getDatabaseName()).append("\n");
            pdfContent.append("SQL: ").append(log.getSql()).append("\n");
            pdfContent.append("Thời gian thực thi: ").append(log.getExeTime()).append("ms\n");
            pdfContent.append("Số lần thực thi: ").append(log.getExeCount()).append("\n");
            pdfContent.append("Thời gian tạo: ").append(log.getCreatedAt()).append("\n");
            pdfContent.append("----------------------------------------\n");
        }
        
        return pdfContent.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Save report to D: drive
     */
    public String saveReportToDrive(byte[] content, String type) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "BaoCaoTongHop_" + timestamp + "." + type.toLowerCase();
        String filePath = "D:\\" + fileName;
        
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(new String(content, StandardCharsets.UTF_8));
        }
        
        return filePath;
    }

    /**
     * Escape CSV field to handle commas and quotes
     */
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        
        return field;
    }
}
