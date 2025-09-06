package com.nhalamphitrentroi.testapp1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DatabaseResponse {
    @JsonProperty("database_name")
    private String databaseName;
    
    @JsonProperty("database_desc")
    private String databaseDesc;
    
    // Default constructor
    public DatabaseResponse() {}
    
    // Constructor with all fields
    public DatabaseResponse(String databaseName, String databaseDesc) {
        this.databaseName = databaseName;
        this.databaseDesc = databaseDesc;
    }
    
    // Getters and Setters
    public String getDatabaseName() {
        return databaseName;
    }
    
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    
    public String getDatabaseDesc() {
        return databaseDesc;
    }
    
    public void setDatabaseDesc(String databaseDesc) {
        this.databaseDesc = databaseDesc;
    }
    
    @Override
    public String toString() {
        return "DatabaseResponse{" +
                "databaseName='" + databaseName + '\'' +
                ", databaseDesc='" + databaseDesc + '\'' +
                '}';
    }
}
