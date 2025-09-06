package com.nhalamphitrentroi.testapp1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhalamphitrentroi.testapp1.config.LogFileProcessor;
import com.nhalamphitrentroi.testapp1.dto.ApiResponse;
import com.nhalamphitrentroi.testapp1.entity.DatabaseLog;
import com.nhalamphitrentroi.testapp1.repository.DatabaseLogRepository;

@RestController
@RequestMapping("/api/verification")
@CrossOrigin(origins = "*")
public class LogVerificationController {
    
    @Autowired
    private DatabaseLogRepository databaseLogRepository;
    
    /**
     * Check if log processing is finished and show statistics
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getProcessingStatus() {
        try {
            long totalLogs = databaseLogRepository.count();
            boolean isFinished = LogFileProcessor.finish_analys;
            
            String status = String.format(
                "Log Processing Status:\n" +
                "Processing Finished: %s\n" +
                "Total Log Entries in Database: %d\n" +
                "finish_analys flag: %s",
                isFinished ? "YES" : "NO",
                totalLogs,
                isFinished
            );
            
            return ResponseEntity.ok(ApiResponse.success(status));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Error checking status: " + e.getMessage()));
        }
    }
    
    /**
     * Get sample log entries to verify data insertion
     */
    @GetMapping("/sample-logs")
    public ResponseEntity<ApiResponse<List<DatabaseLog>>> getSampleLogs() {
        try {
            List<DatabaseLog> logs = databaseLogRepository.findAll();
            
            // Return only first 10 entries for verification
            if (logs.size() > 10) {
                logs = logs.subList(0, 10);
            }
            
            return ResponseEntity.ok(ApiResponse.success(logs));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Error getting sample logs: " + e.getMessage()));
        }
    }
    
    /**
     * Get log count by database name
     */
    @GetMapping("/count-by-database")
    public ResponseEntity<ApiResponse<String>> getLogCountByDatabase() {
        try {
            List<DatabaseLog> allLogs = databaseLogRepository.findAll();
            
            StringBuilder result = new StringBuilder("Log Count by Database:\n");
            
            allLogs.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    DatabaseLog::getDatabaseName,
                    java.util.stream.Collectors.counting()
                ))
                .forEach((dbName, count) -> 
                    result.append(String.format("- %s: %d entries\n", dbName, count))
                );
            
            return ResponseEntity.ok(ApiResponse.success(result.toString()));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Error getting count: " + e.getMessage()));
        }
    }
}
