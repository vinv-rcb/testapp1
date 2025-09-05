package com.nhalamphitrentroi.testapp1.dto;

public class ApiResponse<T> {
    private int status;
    private String errorCode;
    private String errorDesc;
    private T data;
    
    public ApiResponse() {}
    
    public ApiResponse(int status, String errorCode, String errorDesc, T data) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.data = data;
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, null, null, data);
    }
    
    public static <T> ApiResponse<T> error(int status, String errorCode, String errorDesc) {
        return new ApiResponse<>(status, errorCode, errorDesc, null);
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
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
}
