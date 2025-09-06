package com.nhalamphitrentroi.testapp1.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATABASE_LOG", indexes = {
    @Index(name = "idx_database_name", columnList = "database_name"),
    @Index(name = "idx_exe_time", columnList = "exe_time"),
    @Index(name = "idx_exe_count", columnList = "exe_count"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
public class DatabaseLog {
    
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
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public DatabaseLog() {}
    
    // Constructor with all fields
    public DatabaseLog(String databaseName, String sql, Long exeTime, Long exeCount) {
        this.databaseName = databaseName;
        this.sql = sql;
        this.exeTime = exeTime;
        this.exeCount = exeCount;
        this.createdAt = LocalDateTime.now();
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "DatabaseLog{" +
                "id=" + id +
                ", databaseName='" + databaseName + '\'' +
                ", sql='" + sql + '\'' +
                ", exeTime=" + exeTime +
                ", exeCount=" + exeCount +
                ", createdAt=" + createdAt +
                '}';
    }
}
