package com.nhalamphitrentroi.testapp1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatabaseLogResponse {
    @JsonProperty("database_name")
    private String databaseName;
    
    @JsonProperty("sql")
    private String sql;
    
    @JsonProperty("exec_time")
    private Long execTime;
    
    @JsonProperty("exe_count")
    private Long exeCount;
    
    // Default constructor
    public DatabaseLogResponse() {}
    
    // Constructor with all fields
    public DatabaseLogResponse(String databaseName, String sql, Long execTime, Long exeCount) {
        this.databaseName = databaseName;
        this.sql = sql;
        this.execTime = execTime;
        this.exeCount = exeCount;
    }
    
    // Getters and Setters
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
    
    public Long getExecTime() {
        return execTime;
    }
    
    public void setExecTime(Long execTime) {
        this.execTime = execTime;
    }
    
    public Long getExeCount() {
        return exeCount;
    }
    
    public void setExeCount(Long exeCount) {
        this.exeCount = exeCount;
    }
    
    @Override
    public String toString() {
        return "DatabaseLogResponse{" +
                "databaseName='" + databaseName + '\'' +
                ", sql='" + sql + '\'' +
                ", execTime=" + execTime +
                ", exeCount=" + exeCount +
                '}';
    }
}
