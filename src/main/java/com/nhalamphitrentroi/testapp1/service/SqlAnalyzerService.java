package com.nhalamphitrentroi.testapp1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SqlAnalyzerService {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlAnalyzerService.class);
    
    // Pattern to match SELECT statements with FROM and WHERE clauses
    private static final Pattern SELECT_PATTERN = Pattern.compile(
        "SELECT\\s+.*?\\s+FROM\\s+([\\w\\.]+)\\s+WHERE\\s+(.*?)(?:\\s+ORDER\\s+BY|\\s+GROUP\\s+BY|\\s+HAVING|\\s+LIMIT|\\s+OFFSET|$)",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    
    // Pattern to extract column names from WHERE clause
    private static final Pattern WHERE_COLUMN_PATTERN = Pattern.compile(
        "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*[=<>!]+",
        Pattern.CASE_INSENSITIVE
    );
    
    /**
     * Analyze SQL query and extract table name and WHERE clause columns
     * @param sql The SQL query to analyze
     * @return SqlAnalysisResult containing table name and WHERE columns, or null if not a valid SELECT query
     */
    public SqlAnalysisResult analyzeSelectQuery(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            logger.debug("Empty SQL query provided");
            return null;
        }
        
        String trimmedSql = sql.trim();
        logger.debug("Analyzing SQL query: {}", trimmedSql);
        
        // Check if it's a SELECT statement
        if (!trimmedSql.toUpperCase().startsWith("SELECT")) {
            logger.debug("Not a SELECT query: {}", trimmedSql);
            return null;
        }
        
        // Check if it has FROM clause
        if (!trimmedSql.toUpperCase().contains("FROM")) {
            logger.debug("SELECT query without FROM clause: {}", trimmedSql);
            return null;
        }
        
        // Check if it has WHERE clause
        if (!trimmedSql.toUpperCase().contains("WHERE")) {
            logger.debug("SELECT query without WHERE clause: {}", trimmedSql);
            return null;
        }
        
        try {
            Matcher matcher = SELECT_PATTERN.matcher(trimmedSql);
            if (matcher.matches()) {
                String tableName = matcher.group(1).trim();
                String whereClause = matcher.group(2).trim();
                
                logger.debug("Extracted table name: {}", tableName);
                logger.debug("Extracted WHERE clause: {}", whereClause);
                
                // Extract column names from WHERE clause
                List<String> whereColumns = extractWhereColumns(whereClause);
                
                logger.debug("Extracted WHERE columns: {}", whereColumns);
                
                return new SqlAnalysisResult(tableName, whereColumns);
            } else {
                logger.debug("SQL query does not match expected SELECT pattern: {}", trimmedSql);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error analyzing SQL query: {}", trimmedSql, e);
            return null;
        }
    }
    
    /**
     * Extract column names from WHERE clause
     * @param whereClause The WHERE clause string
     * @return List of column names
     */
    private List<String> extractWhereColumns(String whereClause) {
        List<String> columns = new ArrayList<>();
        
        try {
            Matcher matcher = WHERE_COLUMN_PATTERN.matcher(whereClause);
            while (matcher.find()) {
                String column = matcher.group(1).trim();
                if (!column.isEmpty() && !columns.contains(column)) {
                    columns.add(column);
                }
            }
        } catch (Exception e) {
            logger.error("Error extracting WHERE columns from: {}", whereClause, e);
        }
        
        return columns;
    }
    
    /**
     * Generate suggestion text for index recommendation
     * @param tableName The table name
     * @param whereColumns List of columns in WHERE clause
     * @return Suggestion text
     */
    public String generateIndexSuggestion(String tableName, List<String> whereColumns) {
        if (whereColumns == null || whereColumns.isEmpty()) {
            return "Thêm index cho bảng " + tableName;
        }
        
        StringBuilder suggestion = new StringBuilder();
        suggestion.append("Thêm index cho trường ");
        
        for (int i = 0; i < whereColumns.size(); i++) {
            if (i > 0) {
                suggestion.append("; ");
            }
            suggestion.append(whereColumns.get(i));
        }
        
        return suggestion.toString();
    }
    
    /**
     * Result class for SQL analysis
     */
    public static class SqlAnalysisResult {
        private final String tableName;
        private final List<String> whereColumns;
        
        public SqlAnalysisResult(String tableName, List<String> whereColumns) {
            this.tableName = tableName;
            this.whereColumns = whereColumns != null ? new ArrayList<>(whereColumns) : new ArrayList<>();
        }
        
        public String getTableName() {
            return tableName;
        }
        
        public List<String> getWhereColumns() {
            return whereColumns;
        }
        
        @Override
        public String toString() {
            return "SqlAnalysisResult{" +
                    "tableName='" + tableName + '\'' +
                    ", whereColumns=" + whereColumns +
                    '}';
        }
    }
}
