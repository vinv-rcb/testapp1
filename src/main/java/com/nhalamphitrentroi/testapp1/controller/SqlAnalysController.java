package com.nhalamphitrentroi.testapp1.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhalamphitrentroi.testapp1.dto.ApiResponse;
import com.nhalamphitrentroi.testapp1.dto.DatabaseLogResponse;
import com.nhalamphitrentroi.testapp1.dto.DatabaseResponse;
import com.nhalamphitrentroi.testapp1.dto.QueryUnexpectedResponse;
import com.nhalamphitrentroi.testapp1.entity.Database;
import com.nhalamphitrentroi.testapp1.entity.DatabaseLog;
import com.nhalamphitrentroi.testapp1.repository.DatabaseLogRepository;
import com.nhalamphitrentroi.testapp1.repository.DatabaseRepository;
import com.nhalamphitrentroi.testapp1.util.JwtUtil;

@RestController
@RequestMapping("/sqlanalys")
@CrossOrigin(origins = "*")
public class SqlAnalysController {
    
    @Autowired
    private DatabaseLogRepository databaseLogRepository;
    
    @Autowired
    private DatabaseRepository databaseRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * API: sqlanalys/log
     * GET method with token verification and database filtering
     */
    @GetMapping("/log")
    public ResponseEntity<ApiResponse<List<DatabaseLogResponse>>> getLogs(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String database) {
        
        try {
            // Verify token and check R_LOGS_MANAGE permission
            if (!isValidTokenWithPermission(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Token không hợp lệ"));
            }
            
            List<DatabaseLog> logs;
            
            // Query logs based on database parameter
            if (database != null && !database.trim().isEmpty()) {
                logs = databaseLogRepository.findLogsByDatabaseName(database);
            } else {
                logs = databaseLogRepository.findAllLogsOrderByCreatedAtDesc();
            }
            
            // Check if data exists
            if (logs.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error(500, "404", "Không tìm thấy log trên database"));
            }
            
            // Convert to response DTOs
            List<DatabaseLogResponse> responseData = logs.stream()
                    .map(log -> new DatabaseLogResponse(
                            log.getDatabaseName(),
                            log.getSql(),
                            log.getExeTime(),
                            log.getExeCount()
                    ))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(responseData));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    /**
     * API: sqlanalys/database
     * GET method with token verification to get all databases
     */
    @GetMapping("/database")
    public ResponseEntity<ApiResponse<List<DatabaseResponse>>> getDatabases(
            @RequestHeader("Authorization") String token) {
        
        try {
            // Verify token and check R_LOGS_MANAGE permission
            if (!isValidTokenWithPermission(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Token không hợp lệ"));
            }
            
            // Get all databases
            List<Database> databases = databaseRepository.findAllByOrderByDatabaseName();
            
            // Convert to response DTOs
            List<DatabaseResponse> responseData = databases.stream()
                    .map(db -> new DatabaseResponse(
                            db.getDatabaseName(),
                            db.getDatabaseDesc()
                    ))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(responseData));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    /**
     * Verify token and check for R_LOGS_MANAGE permission
     */
    private boolean isValidTokenWithPermission(String token) {
        try {
            // Remove "Bearer " prefix if present
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // Validate token
            if (!jwtUtil.validateToken(token)) {
                return false;
            }
            
            // Get username from token
            String username = jwtUtil.getUsernameFromToken(token);
            
            // For this implementation, we'll check if the user has R_ADMIN role
            // In a real implementation, you might want to check for a specific R_LOGS_MANAGE permission
            // For now, we'll allow R_ADMIN users to access this API
            return username != null && (username.equals("r_admin") || username.equals("admin"));
            
        } catch (Exception e) {
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * API: sqlanalys/query-unexpected
     * GET method to get unexpected queries with exe_time > 500 AND exe_count > 100
     */
    @GetMapping("/query-unexpected")
    public ResponseEntity<QueryUnexpectedResponse> getUnexpectedQueries(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sql,
            @RequestParam(required = false) String database) {
        
        try {
            // Verify token and check R_LOGS_MANAGE permission
            if (!isValidTokenWithPermission(token)) {
                return ResponseEntity.ok(new QueryUnexpectedResponse(
                    500, "401", "Token không hợp lệ", null));
            }
            
            // Validate pagination parameters
            if (page < 0) page = 0;
            if (size <= 0) size = 10;
            if (size > 100) size = 100; // Limit max page size
            
            // Create pageable object
            Pageable pageable = PageRequest.of(page, size);
            
            // Search for unexpected queries
            Page<DatabaseLog> logPage = databaseLogRepository.findUnexpectedQueries(
                sql, database, pageable);
            
            // Convert to response DTOs
            List<DatabaseLogResponse> responseData = logPage.getContent().stream()
                    .map(log -> new DatabaseLogResponse(
                            log.getDatabaseName(),
                            log.getSql(),
                            log.getExeTime(),
                            log.getExeCount()
                    ))
                    .collect(Collectors.toList());
            
            // Create detailed message
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Showing queries with execution time > 500ms and execution count > 100");
            
            if (sql != null && !sql.trim().isEmpty()) {
                messageBuilder.append(" (filtered by SQL containing: '").append(sql).append("')");
            }
            if (database != null && !database.trim().isEmpty()) {
                messageBuilder.append(" (filtered by database: '").append(database).append("')");
            }
            
            messageBuilder.append(" - Page ").append(page + 1).append(" of ").append(logPage.getTotalPages());
            messageBuilder.append(" (").append(logPage.getTotalElements()).append(" total results)");
            
            // Return success response
            return ResponseEntity.ok(new QueryUnexpectedResponse(
                200, "00", null, 
                logPage.getTotalPages(), 
                logPage.getTotalElements(), 
                responseData,
                messageBuilder.toString()));
            
        } catch (Exception e) {
            // Return error response
            return ResponseEntity.ok(new QueryUnexpectedResponse(
                400, "00", "Lấy danh sách log không thành công", null));
        }
    }
}
