package com.nhalamphitrentroi.testapp1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuggestionResponse {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("database_name")
    private String databaseName;
    
    @JsonProperty("sql")
    private String sql;
    
    @JsonProperty("suggestion")
    private String suggestion;
    
    // Default constructor
    public SuggestionResponse() {}
    
    // Constructor with all fields
    public SuggestionResponse(String id, String databaseName, String sql, String suggestion) {
        this.id = id;
        this.databaseName = databaseName;
        this.sql = sql;
        this.suggestion = suggestion;
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
    
    @Override
    public String toString() {
        return "SuggestionResponse{" +
                "id='" + id + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", sql='" + sql + '\'' +
                ", suggestion='" + suggestion + '\'' +
                '}';
    }
}
