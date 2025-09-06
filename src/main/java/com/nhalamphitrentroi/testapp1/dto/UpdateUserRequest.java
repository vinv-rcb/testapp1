package com.nhalamphitrentroi.testapp1.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserRequest {
    
    @NotBlank(message = "Username is required")
    private String username;
    
    private String role;
    private String status;
    
    public UpdateUserRequest() {}
    
    public UpdateUserRequest(String username, String role, String status) {
        this.username = username;
        this.role = role;
        this.status = status;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
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
