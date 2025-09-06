package com.nhalamphitrentroi.testapp1.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATABASE_SUGGESTION")
public class DatabaseSuggestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;
    
    @Column(name = "database_name", length = 50, nullable = false)
    private String databaseName;
    
    @Column(name = "sql", nullable = false, length = 65535)
    private String sql;
    
    @Column(name = "suggestion", nullable = false, length = 65535)
    private String suggestion;
    
    @Column(name = "is_resolved", nullable = false)
    private Boolean isResolved;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public DatabaseSuggestion() {
        this.id = UUID.randomUUID().toString();
        this.isResolved = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public DatabaseSuggestion(String databaseName, String sql, String suggestion) {
        this();
        this.databaseName = databaseName;
        this.sql = sql;
        this.suggestion = suggestion;
    }
    
    // Constructor with all fields including isResolved
    public DatabaseSuggestion(String databaseName, String sql, String suggestion, Boolean isResolved) {
        this();
        this.databaseName = databaseName;
        this.sql = sql;
        this.suggestion = suggestion;
        this.isResolved = isResolved;
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
