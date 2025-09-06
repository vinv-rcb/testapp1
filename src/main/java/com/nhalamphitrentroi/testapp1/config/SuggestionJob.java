package com.nhalamphitrentroi.testapp1.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nhalamphitrentroi.testapp1.entity.DatabaseLog;
import com.nhalamphitrentroi.testapp1.entity.DatabaseSuggestion;
import com.nhalamphitrentroi.testapp1.repository.DatabaseLogRepository;
import com.nhalamphitrentroi.testapp1.repository.DatabaseSuggestionRepository;
import com.nhalamphitrentroi.testapp1.service.SqlAnalyzerService;
import com.nhalamphitrentroi.testapp1.service.SqlAnalyzerService.SqlAnalysisResult;

@Component
public class SuggestionJob {
    
    private static final Logger logger = LoggerFactory.getLogger(SuggestionJob.class);
    private static volatile boolean isRunning = false;
    
    @Autowired
    private DatabaseLogRepository databaseLogRepository;
    
    @Autowired
    private DatabaseSuggestionRepository databaseSuggestionRepository;
    
    @Autowired
    private SqlAnalyzerService sqlAnalyzerService;

    /**
     * Scheduled job to analyze database logs and generate suggestions
     * Runs every 1 minute
     */
    @Scheduled(fixedRate = 60000) // 1 minute in milliseconds
    public void analyzeLogsAndGenerateSuggestions() {
        // Check if job is already running
        if (isRunning) {
            logger.info("Suggestion job is already running, skipping this execution");
            return;
        }
        
        synchronized (SuggestionJob.class) {
            if (isRunning) {
                logger.info("Suggestion job is already running, skipping this execution");
                return;
            }
            isRunning = true;
        }
        
        logger.info("=== STARTING SUGGESTION JOB ===");
        
        try {
            // Get all logs with exe_time > 500 OR exe_count > 100
            List<DatabaseLog> problematicLogs = databaseLogRepository.findProblematicLogs();
            
            logger.info("Found {} problematic logs to analyze", problematicLogs.size());
            
            int processedCount = 0;
            int suggestionCount = 0;
            int errorCount = 0;
            
            for (DatabaseLog log : problematicLogs) {
                try {
                    processedCount++;
                    logger.debug("Processing log {}: {}", processedCount, log.getSql());
                    
                    // Analyze the SQL query
                    SqlAnalysisResult analysis = sqlAnalyzerService.analyzeSelectQuery(log.getSql());
                    
                    if (analysis != null) {
                        // Check if suggestion already exists for this SQL
                        if (!suggestionExists(log.getSql())) {
                            // Generate suggestion
                            String suggestion = sqlAnalyzerService.generateIndexSuggestion(
                                analysis.getTableName(), 
                                analysis.getWhereColumns()
                            );
                            
                            // Try to save suggestion with retry logic
                            boolean saved = false;
                            int maxRetries = 3;
                            
                            for (int retry = 0; retry < maxRetries && !saved; retry++) {
                                try {
                                    // Double-check if suggestion exists before saving (race condition protection)
                                    String sqlHash = generateSqlHash(log.getSql());
                                    if (!databaseSuggestionRepository.existsBySqlHash(sqlHash)) {
                                        // Create a fresh entity to avoid Hibernate state issues
                                        DatabaseSuggestion freshSuggestion = new DatabaseSuggestion(
                                            log.getDatabaseName(),
                                            log.getSql(),
                                            suggestion
                                        );
                                        
                                        databaseSuggestionRepository.saveAndFlush(freshSuggestion);
                                        suggestionCount++;
                                        saved = true;
                                        logger.debug("Created suggestion: {}", suggestion);
                                    } else {
                                        logger.debug("Suggestion already exists for SQL (race condition): {}", log.getSql());
                                        saved = true; // Consider this as "success" since suggestion exists
                                    }
                                } catch (org.hibernate.StaleObjectStateException e) {
                                    if (retry == maxRetries - 1) {
                                        logger.warn("StaleObjectStateException when saving suggestion for SQL: {} - suggestion may have been created by another process", log.getSql());
                                    } else {
                                        logger.debug("StaleObjectStateException on retry {} for SQL: {}, retrying...", retry + 1, log.getSql());
                                        try {
                                            Thread.sleep(100); // Brief pause before retry
                                        } catch (InterruptedException ie) {
                                            Thread.currentThread().interrupt();
                                            break;
                                        }
                                    }
                                } catch (org.springframework.dao.DataIntegrityViolationException e) {
                                    if (e.getMessage().contains("unique") || e.getMessage().contains("duplicate")) {
                                        logger.debug("Suggestion already exists for SQL (unique constraint): {}", log.getSql());
                                        saved = true; // Consider this as "success" since suggestion exists
                                    } else {
                                        logger.warn("DataIntegrityViolationException when saving suggestion for SQL: {} - {}", log.getSql(), e.getMessage());
                                        saved = true; // Don't retry for data integrity issues
                                    }
                                } catch (Exception e) {
                                    if (retry == maxRetries - 1) {
                                        logger.error("Error saving suggestion for SQL: {} - {}", log.getSql(), e.getMessage());
                                        errorCount++;
                                    } else {
                                        logger.debug("Error on retry {} for SQL: {}, retrying...", retry + 1, log.getSql());
                                        try {
                                            Thread.sleep(100); // Brief pause before retry
                                        } catch (InterruptedException ie) {
                                            Thread.currentThread().interrupt();
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            logger.debug("Suggestion already exists for SQL: {}", log.getSql());
                        }
                    } else {
                        logger.debug("SQL query not suitable for analysis: {}", log.getSql());
                    }
                    
                } catch (Exception e) {
                    errorCount++;
                    logger.error("Error processing log {}: {}", processedCount, e.getMessage(), e);
                }
            }
            
            logger.info("=== SUGGESTION JOB COMPLETED ===");
            logger.info("Processed logs: {}", processedCount);
            logger.info("New suggestions created: {}", suggestionCount);
            logger.info("Errors: {}", errorCount);
            
        } catch (Exception e) {
            logger.error("Error in suggestion job: {}", e.getMessage(), e);
        } finally {
            // Reset the running flag
            synchronized (SuggestionJob.class) {
                isRunning = false;
            }
            logger.info("Suggestion job finished, flag reset");
        }
    }
    
    /**
     * Check if a suggestion already exists for the given SQL
     * @param sql The SQL query to check
     * @return true if suggestion exists, false otherwise
     */
    private boolean suggestionExists(String sql) {
        try {
            // Generate hash for the SQL query
            String sqlHash = generateSqlHash(sql);
            // Use hash-based lookup for better performance and to avoid MySQL TEXT column issues
            return databaseSuggestionRepository.existsBySqlHash(sqlHash);
        } catch (Exception e) {
            logger.error("Error checking existing suggestions for SQL: {}", sql, e);
            return false;
        }
    }
    
    /**
     * Generate SHA-256 hash for SQL query (same as in DatabaseSuggestion entity)
     */
    private String generateSqlHash(String sql) {
        if (sql == null) {
            return null;
        }
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sql.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            // Fallback to simple hash if SHA-256 is not available
            return String.valueOf(sql.hashCode());
        }
    }
    
    /**
     * Manual trigger for the suggestion job (for testing purposes)
     */
    public void runSuggestionJobManually() {
        logger.info("Manually triggering suggestion job...");
        analyzeLogsAndGenerateSuggestions();
    }
}
