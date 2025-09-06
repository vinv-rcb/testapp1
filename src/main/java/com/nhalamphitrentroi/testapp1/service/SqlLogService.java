package com.nhalamphitrentroi.testapp1.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.nhalamphitrentroi.testapp1.dto.SqlLogEntry;

@Service
public class SqlLogService {

    private static final String LOG_FILE_PATH = "logsql.log";
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "DB:([^,]+),sql:([^,]+),account_id=(\\d+),exec_time_ms:(\\d+),exec_count:(\\d+)"
    );

    /**
     * Read and parse all entries from the logsql.log file
     * @return List of SqlLogEntry objects
     */
    public List<SqlLogEntry> readAllLogEntries() throws IOException {
        List<SqlLogEntry> entries = new ArrayList<>();
        Path logPath = Paths.get(LOG_FILE_PATH);
        
        if (!Files.exists(logPath)) {
            throw new IOException("Log file not found: " + LOG_FILE_PATH);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                SqlLogEntry entry = parseLogLine(line.trim());
                if (entry != null) {
                    entries.add(entry);
                }
            }
        }
        
        return entries;
    }

    /**
     * Read log entries for a specific account ID
     * @param accountId The account ID to filter by
     * @return List of SqlLogEntry objects for the specified account
     */
    public List<SqlLogEntry> readLogEntriesByAccountId(Long accountId) throws IOException {
        List<SqlLogEntry> allEntries = readAllLogEntries();
        return allEntries.stream()
                .filter(entry -> entry.getAccountId().equals(accountId))
                .toList();
    }

    /**
     * Read log entries with execution time greater than specified threshold
     * @param thresholdMs The execution time threshold in milliseconds
     * @return List of SqlLogEntry objects with execution time above threshold
     */
    public List<SqlLogEntry> readLogEntriesByExecutionTime(Long thresholdMs) throws IOException {
        List<SqlLogEntry> allEntries = readAllLogEntries();
        return allEntries.stream()
                .filter(entry -> entry.getExecTimeMs() > thresholdMs)
                .toList();
    }

    /**
     * Get statistics about the log entries
     * @return String containing statistics summary
     */
    public String getLogStatistics() throws IOException {
        List<SqlLogEntry> entries = readAllLogEntries();
        
        if (entries.isEmpty()) {
            return "No log entries found.";
        }

        long totalEntries = entries.size();
        long totalExecTime = entries.stream().mapToLong(SqlLogEntry::getExecTimeMs).sum();
        long totalExecCount = entries.stream().mapToLong(SqlLogEntry::getExecCount).sum();
        long avgExecTime = totalExecTime / totalEntries;
        long maxExecTime = entries.stream().mapToLong(SqlLogEntry::getExecTimeMs).max().orElse(0);
        long minExecTime = entries.stream().mapToLong(SqlLogEntry::getExecTimeMs).min().orElse(0);

        return """
            Log Statistics:
            Total Entries: %d
            Total Execution Time: %d ms
            Total Execution Count: %d
            Average Execution Time: %d ms
            Max Execution Time: %d ms
            Min Execution Time: %d ms
            """.formatted(totalEntries, totalExecTime, totalExecCount, avgExecTime, maxExecTime, minExecTime);
    }

    /**
     * Parse a single log line into a SqlLogEntry object
     * @param line The log line to parse
     * @return SqlLogEntry object or null if parsing fails
     */
    private SqlLogEntry parseLogLine(String line) {
        if (line.isEmpty()) {
            return null;
        }

        Matcher matcher = LOG_PATTERN.matcher(line);
        if (matcher.matches()) {
            try {
                String database = matcher.group(1);
                String sql = matcher.group(2);
                Long accountId = Long.valueOf(matcher.group(3));
                Long execTimeMs = Long.valueOf(matcher.group(4));
                Long execCount = Long.valueOf(matcher.group(5));

                return new SqlLogEntry(database, sql, accountId, execTimeMs, execCount);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing log line: " + line + " - " + e.getMessage());
                return null;
            }
        } else {
            System.err.println("Log line does not match expected pattern: " + line);
            return null;
        }
    }
}
