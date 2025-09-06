package com.nhalamphitrentroi.testapp1.dto;

import java.util.List;

public class ListUserResponse {
    private int status;
    private String errorCode;
    private String errorDesc;
    private List<UserInfoDto> listUser;
    
    public ListUserResponse() {}
    
    public ListUserResponse(int status, String errorCode, String errorDesc, List<UserInfoDto> listUser) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.listUser = listUser;
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
    
    public List<UserInfoDto> getListUser() {
        return listUser;
    }
    
    public void setListUser(List<UserInfoDto> listUser) {
        this.listUser = listUser;
    }
}
