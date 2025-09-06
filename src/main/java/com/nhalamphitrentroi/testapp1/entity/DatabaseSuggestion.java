package com.nhalamphitrentroi.testapp1.entity;

import java.security.MessageDigest;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "DATABASE_SUGGESTION", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"sql_hash"}, name = "uk_database_suggestion_sql_hash"))
public class DatabaseSuggestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    
    @Column(name = "database_name", length = 50, nullable = false)
    private String databaseName;
    
    @Column(name = "sql", nullable = false, length = 65535)
    private String sql;
    
    @Column(name = "sql_hash", nullable = false, length = 64)
    private String sqlHash;
    
    @Column(name = "suggestion", nullable = false, length = 65535)
    private String suggestion;
    
    @Column(name = "is_resolved", nullable = false)
    private Boolean isResolved;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public DatabaseSuggestion() {
        this.isResolved = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public DatabaseSuggestion(String databaseName, String sql, String suggestion) {
        this();
        this.databaseName = databaseName;
        this.sql = sql;
        this.suggestion = suggestion;
        this.sqlHash = generateSqlHash(sql);
    }
    
    // Constructor with all fields including isResolved
    public DatabaseSuggestion(String databaseName, String sql, String suggestion, Boolean isResolved) {
        this();
        this.databaseName = databaseName;
        this.sql = sql;
        this.suggestion = suggestion;
        this.isResolved = isResolved;
        this.sqlHash = generateSqlHash(sql);
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    
    public String getSql() {
        return sql;
    }
    
    public void setSql(String sql) {
        this.sql = sql;
    }
    
    public String getSuggestion() {
        return suggestion;
    }
    
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
    
    public Boolean getIsResolved() {
        return isResolved;
    }
    
    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getSqlHash() {
        return sqlHash;
    }
    
    public void setSqlHash(String sqlHash) {
        this.sqlHash = sqlHash;
    }
    
    /**
     * Generate SHA-256 hash for SQL query
     */
    private String generateSqlHash(String sql) {
        if (sql == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
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
    
    @Override
    public String toString() {
        return "DatabaseSuggestion{" +
                "id='" + id + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", sql='" + sql + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", isResolved=" + isResolved +
                ", createdAt=" + createdAt +
                '}';
    }
}
