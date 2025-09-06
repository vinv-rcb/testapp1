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
    
    @Autowired
    private DatabaseLogRepository databaseLogRepository;
    
    @Autowired
    private DatabaseSuggestionRepository databaseSuggestionRepository;
    
    @Autowired
    private SqlAnalyzerService sqlAnalyzerService;
    
    /**
     * Scheduled job to analyze database logs and generate suggestions
     * Runs every 30 minutes
     */
    @Scheduled(fixedRate = 1800000) // 30 minutes in milliseconds
    public void analyzeLogsAndGenerateSuggestions() {
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
                            
                            // Create and save suggestion
                            DatabaseSuggestion databaseSuggestion = new DatabaseSuggestion(
                                log.getDatabaseName(),
                                log.getSql(),
                                suggestion
                            );
                            
                            databaseSuggestionRepository.save(databaseSuggestion);
                            suggestionCount++;
                            
                            logger.debug("Created suggestion: {}", suggestion);
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
        }
    }
    
    /**
     * Check if a suggestion already exists for the given SQL
     * @param sql The SQL query to check
     * @return true if suggestion exists, false otherwise
     */
    private boolean suggestionExists(String sql) {
        try {
            List<DatabaseSuggestion> existingSuggestions = databaseSuggestionRepository.findBySqlContaining(sql);
            return !existingSuggestions.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking existing suggestions for SQL: {}", sql, e);
            return false;
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
