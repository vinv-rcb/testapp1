package com.nhalamphitrentroi.testapp1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATABASE")
public class Database {
    
    @Id
    @Column(name = "database_name", length = 50)
    private String databaseName;
    
    @Column(name = "database_desc", length = 255)
    private String databaseDesc;
    
    // Default constructor
    public Database() {}
    
    // Constructor with all fields
    public Database(String databaseName, String databaseDesc) {
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
        return "Database{" +
                "databaseName='" + databaseName + '\'' +
                ", databaseDesc='" + databaseDesc + '\'' +
                '}';
    }
}
