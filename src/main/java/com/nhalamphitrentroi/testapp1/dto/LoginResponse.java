package com.nhalamphitrentroi.testapp1.dto;

import java.time.LocalDate;

public class LoginResponse {
    private String token;
    private String username;
    private String name;
    private LocalDate joinDate;
    private String phoneNumber;
    private String email;
    private String role;
    
    public LoginResponse() {}
    
    public LoginResponse(String token, String username, String name, LocalDate joinDate, 
                        String phoneNumber, String email, String role) {
        this.token = token;
        this.username = username;
        this.name = name;
        this.joinDate = joinDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
