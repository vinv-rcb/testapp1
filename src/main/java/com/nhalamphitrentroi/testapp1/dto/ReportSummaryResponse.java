package com.nhalamphitrentroi.testapp1.dto;

/**
 * DTO for report summary response
 */
public class ReportSummaryResponse {
    private int status;
    private String errorcode;
    private String errordes;
    private long total;
    private long totalUnexpected;
    private long totalHint;

    public ReportSummaryResponse() {}

    public ReportSummaryResponse(int status, String errorcode, String errordes, 
                               long total, long totalUnexpected, long totalHint) {
        this.status = status;
        this.errorcode = errorcode;
        this.errordes = errordes;
        this.total = total;
        this.totalUnexpected = totalUnexpected;
        this.totalHint = totalHint;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrordes() {
        return errordes;
    }

    public void setErrordes(String errordes) {
        this.errordes = errordes;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalUnexpected() {
        return totalUnexpected;
    }

    public void setTotalUnexpected(long totalUnexpected) {
        this.totalUnexpected = totalUnexpected;
    }

    public long getTotalHint() {
        return totalHint;
    }

    public void setTotalHint(long totalHint) {
        this.totalHint = totalHint;
    }
}
