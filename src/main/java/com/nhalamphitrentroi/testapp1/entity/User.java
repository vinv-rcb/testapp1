package com.nhalamphitrentroi.testapp1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false)
    private String title;
    
    @NotBlank(message = "Department is required")
    @Column(name = "department", nullable = false)
    private String department;
    
    @NotBlank(message = "Username is required")
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;
    
    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull(message = "Join date is required")
    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;
    
    @NotBlank(message = "Phone number is required")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "Role is required")
    @Column(name = "role", nullable = false)
    private String role;
    
    // Constructors
    public User() {}
    
    public User(String title, String department, String username, String password, 
                String name, LocalDate joinDate, String phoneNumber, String email, String role) {
        this.title = title;
        this.department = department;
        this.username = username;
        this.password = password;
        this.name = name;
        this.joinDate = joinDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
