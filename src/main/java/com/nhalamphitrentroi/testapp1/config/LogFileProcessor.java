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
import com.nhalamphitrentroi.testapp1.repository.DatabaseLogRepository;

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
        int lineNumber = 0;
        int processedCount = 0;
        int errorCount = 0;
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
                
                DatabaseLog logEntry = parseLogLine(line, lineNumber);
                if (logEntry != null) {
                    batchLogs.add(logEntry);
                    processedCount++;
                    logger.debug("Successfully parsed line {}: DB={}, SQL={}, Time={}ms, Count={}", 
                        lineNumber, logEntry.getDatabaseName(), logEntry.getSql(), 
                        logEntry.getExeTime(), logEntry.getExeCount());
                    
                    // Process in batches of 100
                    if (batchLogs.size() >= 100) {
                        logger.info("Saving batch of {} log entries to database...", batchLogs.size());
                        databaseLogRepository.saveAll(batchLogs);
                        logger.info("Successfully saved {} entries to database", batchLogs.size());
                        batchLogs.clear();
                    }
                } else {
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
            
            long endTime = System.currentTimeMillis();
            long processingTime = endTime - startTime;
            
            logger.info("=== LOG PROCESSING SUMMARY ===");
            logger.info("Total lines processed: {}", lineNumber);
            logger.info("Successfully processed: {}", processedCount);
            logger.info("Errors: {}", errorCount);
            logger.info("Processing time: {} ms", processingTime);
            logger.info("Average time per line: {} ms", lineNumber > 0 ? processingTime / lineNumber : 0);
            
        } catch (IOException e) {
            logger.error("Error reading log file: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    private DatabaseLog parseLogLine(String line, int lineNumber) {
        logger.debug("Parsing line {}: {}", lineNumber, line);
        
        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            try {
                String databaseName = matcher.group(1);
                String sql = matcher.group(2);
                Long execTime = Long.valueOf(matcher.group(3));
                Long execCount = Long.valueOf(matcher.group(4));
                
                logger.debug("Successfully parsed line {}: DB={}, SQL={}, Time={}, Count={}", 
                    lineNumber, databaseName, sql, execTime, execCount);
                
                return new DatabaseLog(databaseName, sql, execTime, execCount);
            } catch (NumberFormatException e) {
                logger.error("Dòng - {} sai format (NumberFormatException): {} - Error: {}", 
                    lineNumber, line, e.getMessage());
                return null;
            }
        } else {
            logger.error("Dòng - {} sai format (Pattern mismatch): {}", lineNumber, line);
            logger.debug("Expected pattern: DB:<database>,sql:<sql>,exec_time_ms:<number>,exec_count:<number>");
            return null;
        }
    }
}
