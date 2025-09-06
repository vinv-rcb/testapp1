package com.nhalamphitrentroi.testapp1.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryUnexpectedResponse {
    @JsonProperty("status")
    private int status;
    
    @JsonProperty("errorcode")
    private String errorCode;
    
    @JsonProperty("errordes")
    private String errorDesc;
    
    @JsonProperty("totalPages")
    private int totalPages;
    
    @JsonProperty("totalElements")
    private long totalElements;
    
    @JsonProperty("listLog")
    private List<DatabaseLogResponse> listLog;
    
    @JsonProperty("message")
    private String message;
    
    // Default constructor
    public QueryUnexpectedResponse() {}
    
    // Constructor for success response
    public QueryUnexpectedResponse(int status, String errorCode, String errorDesc, 
                                 int totalPages, long totalElements, List<DatabaseLogResponse> listLog) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.listLog = listLog;
        this.message = "Showing queries with execution time > 500ms and execution count > 100";
    }
    
    // Constructor for success response with custom message
    public QueryUnexpectedResponse(int status, String errorCode, String errorDesc, 
                                 int totalPages, long totalElements, List<DatabaseLogResponse> listLog, String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.listLog = listLog;
        this.message = message;
    }
    
    // Constructor for error response
    public QueryUnexpectedResponse(int status, String errorCode, String errorDesc, List<DatabaseLogResponse> listLog) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.listLog = listLog;
        this.message = null;
    }
    
    // Getters and Setters
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorDesc() {
        return errorDesc;
    }
    
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public long getTotalElements() {
        return totalElements;
    }
    
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
    
    public List<DatabaseLogResponse> getListLog() {
        return listLog;
    }
    
    public void setListLog(List<DatabaseLogResponse> listLog) {
        this.listLog = listLog;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "QueryUnexpectedResponse{" +
                "status=" + status +
                ", errorCode='" + errorCode + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", listLog=" + listLog +
                ", message='" + message + '\'' +
                '}';
    }
}
