package com.nhalamphitrentroi.testapp1.dto;

/**
 * DTO for report request
 */
public class ReportRequest {
    private String type; // CSV or PDF

    public ReportRequest() {}

    public ReportRequest(String type) {
        this.type = type;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
