package com.nhalamphitrentroi.testapp1.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.nhalamphitrentroi.testapp1.entity.DatabaseLog;
import com.nhalamphitrentroi.testapp1.entity.IncorrectSql;
import com.nhalamphitrentroi.testapp1.repository.DatabaseLogRepository;
import com.nhalamphitrentroi.testapp1.repository.IncorrectSqlRepository;

@Component
public class LogFileProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(LogFileProcessor.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static boolean finish_analys = false;
    
    private static final String LOG_FILE_PATH = "logsql.log";
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "DB:([^,]+),sql:([^,]+),exec_time_ms:(\\d+),exec_count:(\\d+)"
    );
    
    @Autowired
    private DatabaseLogRepository databaseLogRepository;
    
    @Autowired
    private IncorrectSqlRepository incorrectSqlRepository;
    
    @EventListener(ApplicationReadyEvent.class)
    public void processLogFile() {
        logger.info("=== LOG FILE PROCESSOR STARTED ===");
        logger.info("Event listener triggered at: {}", LocalDateTime.now().format(TIME_FORMATTER));
        
        new Thread(() -> {
            try {
                logger.info("Starting log file processing in background thread...");
                logger.info("Thread ID: {}", Thread.currentThread().getId());
                logger.info("Thread Name: {}", Thread.currentThread().getName());
                
                processLogFileInternal();
                
                finish_analys = true;
                logger.info("=== LOG FILE PROCESSING COMPLETED ===");
                logger.info("finish_analys flag set to: {}", finish_analys);
                logger.info("Processing completed at: {}", LocalDateTime.now().format(TIME_FORMATTER));
                
            } catch (Exception e) {
                logger.error("Error processing log file: {}", e.getMessage(), e);
                finish_analys = true; // Set to true even on error to prevent infinite waiting
            }
        }, "LogFileProcessor-Thread").start();
    }
    
    private void processLogFileInternal() throws IOException {
        Path logPath = Paths.get(LOG_FILE_PATH);
        
        logger.info("Checking for log file at path: {}", LOG_FILE_PATH);
        logger.info("Absolute path: {}", logPath.toAbsolutePath());
        
        if (!Files.exists(logPath)) {
            logger.warn("Log file not found: {}. Skipping processing.", LOG_FILE_PATH);
            logger.warn("Please ensure logsql.log file exists in the project root directory");
            return;
        }
        
        logger.info("Log file found! File size: {} bytes", Files.size(logPath));
        logger.info("Starting to read and process log file...");
        
        List<DatabaseLog> batchLogs = new ArrayList<>();
        List<IncorrectSql> incorrectSqlBatch = new ArrayList<>();
        int lineNumber = 0;
        int processedCount = 0;
        int errorCount = 0;
        int incorrectSqlCount = 0;
        long startTime = System.currentTimeMillis();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                if (lineNumber % 10 == 0) {
                    logger.debug("Processing line {}: {}", lineNumber, line);
                }
                
                if (line.isEmpty()) {
                    logger.debug("Skipping empty line {}", lineNumber);
                    continue;
                }
                
                ParseResult parseResult = parseLogLine(line, lineNumber);
                if (parseResult.isValid()) {
                    batchLogs.add(parseResult.getDatabaseLog());
                    processedCount++;
                    logger.debug("Successfully parsed line {}: DB={}, SQL={}, Time={}ms, Count={}", 
                        lineNumber, parseResult.getDatabaseLog().getDatabaseName(), 
                        parseResult.getDatabaseLog().getSql(), 
                        parseResult.getDatabaseLog().getExeTime(), 
                        parseResult.getDatabaseLog().getExeCount());
                    
                    // Process in batches of 100
                    if (batchLogs.size() >= 100) {
                        logger.info("Saving batch of {} log entries to database...", batchLogs.size());
                        databaseLogRepository.saveAll(batchLogs);
                        logger.info("Successfully saved {} entries to database", batchLogs.size());
                        batchLogs.clear();
                    }
                } else if (parseResult.getIncorrectSql() != null) {
                    // SQL format is incorrect, save to incorrect_sql table
                    incorrectSqlBatch.add(parseResult.getIncorrectSql());
                    incorrectSqlCount++;
                    logger.warn("Incorrect SQL format at line {}: {} - Reason: {}", 
                        lineNumber, line, parseResult.getErrorReason());
                    
                    // Process incorrect SQL in batches of 50
                    if (incorrectSqlBatch.size() >= 50) {
                        logger.info("Saving batch of {} incorrect SQL entries to database...", incorrectSqlBatch.size());
                        incorrectSqlRepository.saveAll(incorrectSqlBatch);
                        logger.info("Successfully saved {} incorrect SQL entries to database", incorrectSqlBatch.size());
                        incorrectSqlBatch.clear();
                    }
                } else {
                    // General parsing error (pattern mismatch, number format, etc.)
                    errorCount++;
                    logger.warn("Failed to parse line {}: {}", lineNumber, line);
                }
            }
            
            // Process remaining logs
            if (!batchLogs.isEmpty()) {
                logger.info("Saving final batch of {} log entries to database...", batchLogs.size());
                databaseLogRepository.saveAll(batchLogs);
                logger.info("Successfully saved final {} entries to database", batchLogs.size());
            }
            
            // Process remaining incorrect SQL entries
            if (!incorrectSqlBatch.isEmpty()) {
                logger.info("Saving final batch of {} incorrect SQL entries to database...", incorrectSqlBatch.size());
                incorrectSqlRepository.saveAll(incorrectSqlBatch);
                logger.info("Successfully saved final {} incorrect SQL entries to database", incorrectSqlBatch.size());
            }
            
            long endTime = System.currentTimeMillis();
            long processingTime = endTime - startTime;
            
            logger.info("=== LOG PROCESSING SUMMARY ===");
            logger.info("Total lines processed: {}", lineNumber);
            logger.info("Successfully processed: {}", processedCount);
            logger.info("Incorrect SQL format: {}", incorrectSqlCount);
            logger.info("General errors: {}", errorCount);
            logger.info("Processing time: {} ms", processingTime);
            logger.info("Average time per line: {} ms", lineNumber > 0 ? processingTime / lineNumber : 0);
            
        } catch (IOException e) {
            logger.error("Error reading log file: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Inner class to hold parsing results
     */
    private static class ParseResult {
        private final boolean valid;
        private final DatabaseLog databaseLog;
        private final IncorrectSql incorrectSql;
        private final String errorReason;
        
        private ParseResult(boolean valid, DatabaseLog databaseLog, IncorrectSql incorrectSql, String errorReason) {
            this.valid = valid;
            this.databaseLog = databaseLog;
            this.incorrectSql = incorrectSql;
            this.errorReason = errorReason;
        }
        
        public static ParseResult valid(DatabaseLog databaseLog) {
            return new ParseResult(true, databaseLog, null, null);
        }
        
        public static ParseResult incorrectSql(IncorrectSql incorrectSql, String errorReason) {
            return new ParseResult(false, null, incorrectSql, errorReason);
        }
        
        public static ParseResult error(String errorReason) {
            return new ParseResult(false, null, null, errorReason);
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public DatabaseLog getDatabaseLog() {
            return databaseLog;
        }
        
        public IncorrectSql getIncorrectSql() {
            return incorrectSql;
        }
        
        public String getErrorReason() {
            return errorReason;
        }
    }
    
    /**
     * Validates SQL format to ensure it follows proper SELECT statement structure
     * Valid format: SELECT * FROM table_name WHERE condition
     * Invalid format: SELECT * FROM table_name condition (missing WHERE)
     */
    private boolean isValidSqlFormat(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return false;
        }
        
        String trimmedSql = sql.trim().toUpperCase();
        
        // Check if it starts with SELECT
        if (!trimmedSql.startsWith("SELECT")) {
            return false;
        }
        
        // Check if it contains FROM
        if (!trimmedSql.contains("FROM")) {
            return false;
        }
        
        // Check if it has WHERE clause (for conditions)
        if (trimmedSql.contains("WHERE")) {
            // Valid format: SELECT ... FROM ... WHERE ...
            return true;
        } else {
            // Check if it's a simple SELECT without conditions
            // Pattern: SELECT * FROM table_name (no conditions)
            String[] parts = trimmedSql.split("\\s+");
            boolean hasSelect = false;
            boolean hasFrom = false;
            boolean hasTable = false;
            int fromIndex = -1;
            
            for (int i = 0; i < parts.length; i++) {
                if ("SELECT".equals(parts[i])) {
                    hasSelect = true;
                } else if ("FROM".equals(parts[i])) {
                    hasFrom = true;
                    fromIndex = i;
                    // Check if next part is a table name
                    if (i + 1 < parts.length && !parts[i + 1].isEmpty()) {
                        hasTable = true;
                    }
                }
            }
            
            // Additional validation: after FROM table_name, there should be nothing else
            // unless it's a proper WHERE clause
            if (hasSelect && hasFrom && hasTable && fromIndex >= 0) {
                // Check if there are any words after the table name
                if (fromIndex + 2 < parts.length) {
                    // There are words after table name, this is invalid for simple SELECT
                    return false;
                }
                return true;
            }
            
            return false;
        }
    }
    
    private ParseResult parseLogLine(String line, int lineNumber) {
        logger.debug("Parsing line {}: {}", lineNumber, line);
        
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            try {
                String databaseName = matcher.group(1);
                String sql = matcher.group(2);
                Long execTime = Long.valueOf(matcher.group(3));
                Long execCount = Long.valueOf(matcher.group(4));
                
                // Validate SQL format
                if (!isValidSqlFormat(sql)) {
                    String errorReason = "SQL không đúng định dạng - thiếu WHERE clause hoặc format không hợp lệ";
                    IncorrectSql incorrectSql = new IncorrectSql(databaseName, sql, execTime, execCount, lineNumber, errorReason);
                    logger.error("Dòng - {} sai format SQL: {} - SQL không đúng định dạng", lineNumber, line);
                    return ParseResult.incorrectSql(incorrectSql, errorReason);
                }
                
                logger.debug("Successfully parsed line {}: DB={}, SQL={}, Time={}, Count={}", 
                    lineNumber, databaseName, sql, execTime, execCount);
                
                DatabaseLog databaseLog = new DatabaseLog(databaseName, sql, execTime, execCount);
                return ParseResult.valid(databaseLog);
            } catch (NumberFormatException e) {
                String errorReason = "NumberFormatException: " + e.getMessage();
                logger.error("Dòng - {} sai format (NumberFormatException): {} - Error: {}", 
                    lineNumber, line, e.getMessage());
                return ParseResult.error(errorReason);
            }
        } else {
            String errorReason = "Pattern mismatch - không khớp với định dạng mong đợi";
            logger.error("Dòng - {} sai format (Pattern mismatch): {}", lineNumber, line);
            logger.debug("Expected pattern: DB:<database>,sql:<sql>,exec_time_ms:<number>,exec_count:<number>");
            return ParseResult.error(errorReason);
        }
    }
}
