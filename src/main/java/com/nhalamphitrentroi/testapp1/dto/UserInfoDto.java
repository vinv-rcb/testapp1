package com.nhalamphitrentroi.testapp1.dto;

public class UserInfoDto {
    private String username;
    private String phone;
    private String email;
    private String role;
    private String status;
    
    public UserInfoDto() {}
    
    public UserInfoDto(String username, String phone, String email, String role, String status) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.status = status;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
