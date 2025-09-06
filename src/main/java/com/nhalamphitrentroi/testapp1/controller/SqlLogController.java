package com.nhalamphitrentroi.testapp1.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhalamphitrentroi.testapp1.dto.ApiResponse;
import com.nhalamphitrentroi.testapp1.dto.SqlLogEntry;
import com.nhalamphitrentroi.testapp1.service.SqlLogService;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
public class SqlLogController {

    @Autowired
    private SqlLogService sqlLogService;

    /**
     * Get all SQL log entries
     * @return ResponseEntity containing all log entries
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SqlLogEntry>>> getAllLogEntries() {
        try {
            List<SqlLogEntry> entries = sqlLogService.readAllLogEntries();
            return ResponseEntity.ok(ApiResponse.success(entries));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "LOG_READ_ERROR", "Failed to read log file: " + e.getMessage()));
        }
    }

    /**
     * Get SQL log entries by account ID
     * @param accountId The account ID to filter by
     * @return ResponseEntity containing filtered log entries
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<SqlLogEntry>>> getLogEntriesByAccountId(@PathVariable Long accountId) {
        try {
            List<SqlLogEntry> entries = sqlLogService.readLogEntriesByAccountId(accountId);
            return ResponseEntity.ok(ApiResponse.success(entries));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "LOG_READ_ERROR", "Failed to read log file: " + e.getMessage()));
        }
    }

    /**
     * Get SQL log entries with execution time above threshold
     * @param thresholdMs The execution time threshold in milliseconds
     * @return ResponseEntity containing filtered log entries
     */
    @GetMapping("/slow-queries")
    public ResponseEntity<ApiResponse<List<SqlLogEntry>>> getSlowQueries(@RequestParam Long thresholdMs) {
        try {
            List<SqlLogEntry> entries = sqlLogService.readLogEntriesByExecutionTime(thresholdMs);
            return ResponseEntity.ok(ApiResponse.success(entries));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "LOG_READ_ERROR", "Failed to read log file: " + e.getMessage()));
        }
    }

    /**
     * Get statistics about the SQL log entries
     * @return ResponseEntity containing log statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<String>> getLogStatistics() {
        try {
            String statistics = sqlLogService.getLogStatistics();
            return ResponseEntity.ok(ApiResponse.success(statistics));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "LOG_READ_ERROR", "Failed to read log file: " + e.getMessage()));
        }
    }

    /**
     * Get log entries with custom filtering
     * @param accountId Optional account ID filter
     * @param minExecTime Optional minimum execution time filter
     * @param maxExecTime Optional maximum execution time filter
     * @return ResponseEntity containing filtered log entries
     */
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<SqlLogEntry>>> getFilteredLogEntries(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long minExecTime,
            @RequestParam(required = false) Long maxExecTime) {
        try {
            List<SqlLogEntry> entries = sqlLogService.readAllLogEntries();
            
            // Apply filters
            if (accountId != null) {
                entries = entries.stream()
                        .filter(entry -> entry.getAccountId().equals(accountId))
                        .toList();
            }
            
            if (minExecTime != null) {
                entries = entries.stream()
                        .filter(entry -> entry.getExecTimeMs() >= minExecTime)
                        .toList();
            }
            
            if (maxExecTime != null) {
                entries = entries.stream()
                        .filter(entry -> entry.getExecTimeMs() <= maxExecTime)
                        .toList();
            }
            
            return ResponseEntity.ok(ApiResponse.success(entries));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "LOG_READ_ERROR", "Failed to read log file: " + e.getMessage()));
        }
    }
}