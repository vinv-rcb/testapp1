package com.nhalamphitrentroi.testapp1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SqlLogEntry {
    @JsonProperty("database")
    private String database;
    
    @JsonProperty("sql")
    private String sql;
    
    @JsonProperty("account_id")
    private Long accountId;
    
    @JsonProperty("exec_time_ms")
    private Long execTimeMs;
    
    @JsonProperty("exec_count")
    private Long execCount;

    // Default constructor
    public SqlLogEntry() {}

    // Constructor with all fields
    public SqlLogEntry(String database, String sql, Long accountId, Long execTimeMs, Long execCount) {
        this.database = database;
        this.sql = sql;
        this.accountId = accountId;
        this.execTimeMs = execTimeMs;
        this.execCount = execCount;
    }

    // Getters and Setters
    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getExecTimeMs() {
        return execTimeMs;
    }

    public void setExecTimeMs(Long execTimeMs) {
        this.execTimeMs = execTimeMs;
    }

    public Long getExecCount() {
        return execCount;
    }

    public void setExecCount(Long execCount) {
        this.execCount = execCount;
    }

    @Override
    public String toString() {
        return "SqlLogEntry{" +
                "database='" + database + '\'' +
                ", sql='" + sql + '\'' +
                ", accountId=" + accountId +
                ", execTimeMs=" + execTimeMs +
                ", execCount=" + execCount +
                '}';
    }
}
