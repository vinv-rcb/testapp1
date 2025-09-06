package com.nhalamphitrentroi.testapp1.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "INCORRECT_SQL")
public class IncorrectSql {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "database_name", length = 50, nullable = false)
    private String databaseName;
    
    @Column(name = "sql", nullable = false, length = 65535)
    private String sql;
    
    @Column(name = "exe_time", nullable = false)
    private Long exeTime;
    
    @Column(name = "exe_count", nullable = false)
    private Long exeCount;
    
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;
    
    @Column(name = "error_reason", length = 500)
    private String errorReason;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public IncorrectSql() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public IncorrectSql(String databaseName, String sql, Long exeTime, Long exeCount, 
                       Integer lineNumber, String errorReason) {
        this();
        this.databaseName = databaseName;
        this.sql = sql;
        this.exeTime = exeTime;
        this.exeCount = exeCount;
        this.lineNumber = lineNumber;
        this.errorReason = errorReason;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
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
    
    public Long getExeTime() {
        return exeTime;
    }
    
    public void setExeTime(Long exeTime) {
        this.exeTime = exeTime;
    }
    
    public Long getExeCount() {
        return exeCount;
    }
    
    public void setExeCount(Long exeCount) {
        this.exeCount = exeCount;
    }
    
    public Integer getLineNumber() {
        return lineNumber;
    }
    
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public String getErrorReason() {
        return errorReason;
    }
    
    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "IncorrectSql{" +
                "id=" + id +
                ", databaseName='" + databaseName + '\'' +
                ", sql='" + sql + '\'' +
                ", exeTime=" + exeTime +
                ", exeCount=" + exeCount +
                ", lineNumber=" + lineNumber +
                ", errorReason='" + errorReason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
