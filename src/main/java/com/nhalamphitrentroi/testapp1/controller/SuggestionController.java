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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhalamphitrentroi.testapp1.config.SuggestionJob;
import com.nhalamphitrentroi.testapp1.dto.ApiResponse;
import com.nhalamphitrentroi.testapp1.dto.SuggestionDoneRequest;
import com.nhalamphitrentroi.testapp1.dto.SuggestionResponse;
import com.nhalamphitrentroi.testapp1.entity.DatabaseSuggestion;
import com.nhalamphitrentroi.testapp1.repository.DatabaseSuggestionRepository;
import com.nhalamphitrentroi.testapp1.util.JwtUtil;

@RestController
@RequestMapping("/sqlanalys")
@CrossOrigin(origins = "*")
public class SuggestionController {
    
    @Autowired
    private DatabaseSuggestionRepository databaseSuggestionRepository;
    
    @Autowired
    private SuggestionJob suggestionJob;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Get all suggestions with pagination
     */
    @GetMapping("/suggestions")
    public ResponseEntity<ApiResponse<Page<DatabaseSuggestion>>> getAllSuggestions(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            // Verify token
            if (!isValidTokenWithPermission(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Token không hợp lệ"));
            }
            
            // Validate pagination parameters
            if (page < 0) page = 0;
            if (size <= 0) size = 10;
            if (size > 100) size = 100;
            
            Pageable pageable = PageRequest.of(page, size);
            Page<DatabaseSuggestion> suggestions = databaseSuggestionRepository.findAll(pageable);
            
            return ResponseEntity.ok(ApiResponse.success(suggestions));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    /**
     * Get suggestions by database name
     */
    @GetMapping("/suggestions/database/{databaseName}")
    public ResponseEntity<ApiResponse<List<DatabaseSuggestion>>> getSuggestionsByDatabase(
            @RequestHeader("Authorization") String token,
            @PathVariable String databaseName) {
        
        try {
            // Verify token
            if (!isValidTokenWithPermission(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Token không hợp lệ"));
            }
            
            List<DatabaseSuggestion> suggestions = databaseSuggestionRepository
                .findByDatabaseNameOrderByCreatedAtDesc(databaseName);
            
            return ResponseEntity.ok(ApiResponse.success(suggestions));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    /**
     * Get suggestions by resolution status
     */
    @GetMapping("/suggestions/resolved/{isResolved}")
    public ResponseEntity<ApiResponse<List<DatabaseSuggestion>>> getSuggestionsByResolution(
            @RequestHeader("Authorization") String token,
            @PathVariable Boolean isResolved) {
        
        try {
            // Verify token
            if (!isValidTokenWithPermission(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Token không hợp lệ"));
            }
            
            List<DatabaseSuggestion> suggestions = databaseSuggestionRepository
                .findByIsResolvedOrderByCreatedAtDesc(isResolved);
            
            return ResponseEntity.ok(ApiResponse.success(suggestions));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    /**
     * Get suggestion statistics
     */
    @GetMapping("/suggestions/statistics")
    public ResponseEntity<ApiResponse<String>> getSuggestionStatistics(
            @RequestHeader("Authorization") String token) {
        
        try {
            // Verify token
            if (!isValidTokenWithPermission(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Token không hợp lệ"));
            }
            
            long totalSuggestions = databaseSuggestionRepository.count();
            long resolvedSuggestions = databaseSuggestionRepository.countByIsResolved(true);
            long unresolvedSuggestions = databaseSuggestionRepository.countByIsResolved(false);
            
            String statistics = String.format(
                "Suggestion Statistics:\n" +
                "Total Suggestions: %d\n" +
                "Resolved: %d\n" +
                "Unresolved: %d\n" +
                "Resolution Rate: %.2f%%",
                totalSuggestions,
                resolvedSuggestions,
                unresolvedSuggestions,
                totalSuggestions > 0 ? (double) resolvedSuggestions / totalSuggestions * 100 : 0
            );
            
            return ResponseEntity.ok(ApiResponse.success(statistics));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    /**
     * Manually trigger suggestion job
     */
    @PostMapping("/suggestions/trigger-job")
    public ResponseEntity<ApiResponse<String>> triggerSuggestionJob(
            @RequestHeader("Authorization") String token) {
        
        try {
            // Verify token
            if (!isValidTokenWithPermission(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Token không hợp lệ"));
            }
            
            suggestionJob.runSuggestionJobManually();
            
            return ResponseEntity.ok(ApiResponse.success("Suggestion job triggered successfully"));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    /**
     * API: sqlanalys/suggestion
     * Get unresolved suggestions by database name
     */
    @GetMapping("/suggestion")
    public ResponseEntity<ApiResponse<List<SuggestionResponse>>> getSuggestions(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String database) {
        
        try {
            // Verify token and check R_OPTI permission
            if (!isValidTokenWithROPTI(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Bạn không có quyền truy cập"));
            }
            
            List<DatabaseSuggestion> suggestions;
            
            if (database != null && !database.trim().isEmpty()) {
                // Get suggestions for specific database
                suggestions = databaseSuggestionRepository.findByDatabaseNameAndIsResolvedOrderByCreatedAtDesc(
                    database, false);
            } else {
                // Get all unresolved suggestions
                suggestions = databaseSuggestionRepository.findByIsResolvedOrderByCreatedAtDesc(false);
            }
            
            if (suggestions.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error(500, "404", "Không có gợi ý nào"));
            }
            
            // Convert to response DTOs
            List<SuggestionResponse> responseData = suggestions.stream()
                    .map(suggestion -> new SuggestionResponse(
                            suggestion.getId(),
                            suggestion.getDatabaseName(),
                            suggestion.getSql(),
                            suggestion.getSuggestion()
                    ))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(responseData));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
    
    /**
     * API: sqlanalys/suggestion/done
     * Mark suggestion as resolved
     */
    @PostMapping("/suggestion/done")
    public ResponseEntity<ApiResponse<String>> markSuggestionDone(
            @RequestHeader("Authorization") String token,
            @RequestBody SuggestionDoneRequest request) {
        
        try {
            // Verify token and check R_OPTI permission
            if (!isValidTokenWithROPTI(token)) {
                return ResponseEntity.ok(ApiResponse.error(500, "401", "Bạn không có quyền truy cập"));
            }
            
            if (request.getId() == null || request.getId().trim().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error(500, "400", "ID không hợp lệ"));
            }
            
            // Find the suggestion by ID
            DatabaseSuggestion suggestion = databaseSuggestionRepository.findById(request.getId()).orElse(null);
            
            if (suggestion == null) {
                return ResponseEntity.ok(ApiResponse.error(500, "404", "Không tìm thấy gợi ý"));
            }
            
            // Update is_resolved to true
            suggestion.setIsResolved(true);
            databaseSuggestionRepository.save(suggestion);
            
            return ResponseEntity.ok(ApiResponse.success("Gợi ý đã được đánh dấu hoàn thành"));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(500, "500", "Có lỗi xảy ra. Vui lòng thử lại."));
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
            return username != null && (username.equals("r_admin") || username.equals("admin"));
            
        } catch (Exception e) {
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify token and check for R_OPTI permission
     */
    private boolean isValidTokenWithROPTI(String token) {
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
            
            // For this implementation, we'll check if the user has R_OPTI role
            // You can modify this logic based on your actual role checking mechanism
            return username != null && (username.equals("r_admin") || username.equals("admin") || username.equals("r_opti"));
            
        } catch (Exception e) {
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }
}
