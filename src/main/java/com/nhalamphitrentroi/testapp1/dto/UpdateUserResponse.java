package com.nhalamphitrentroi.testapp1.dto;

public class UpdateUserResponse {
    private int status;
    private String errorCode;
    private String errorDesc;
    
    public UpdateUserResponse() {}
    
    public UpdateUserResponse(int status, String errorCode, String errorDesc) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }
    
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
}
