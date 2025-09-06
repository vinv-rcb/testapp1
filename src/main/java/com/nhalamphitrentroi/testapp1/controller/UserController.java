package com.nhalamphitrentroi.testapp1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhalamphitrentroi.testapp1.dto.ApiResponse;
import com.nhalamphitrentroi.testapp1.dto.ListUserResponse;
import com.nhalamphitrentroi.testapp1.dto.LoginRequest;
import com.nhalamphitrentroi.testapp1.dto.LoginResponse;
import com.nhalamphitrentroi.testapp1.dto.RegisterRequest;
import com.nhalamphitrentroi.testapp1.dto.RegisterResponse;
import com.nhalamphitrentroi.testapp1.dto.UpdateUserRequest;
import com.nhalamphitrentroi.testapp1.dto.UpdateUserResponse;
import com.nhalamphitrentroi.testapp1.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sqlanalys")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success(loginResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "INVALID_CREDENTIALS", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, "INTERNAL_SERVER_ERROR", "An unexpected error occurred"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            RegisterResponse registerResponse = userService.register(registerRequest);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RegisterResponse(500, "INTERNAL_SERVER_ERROR", "An unexpected error occurred"));
        }
    }

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<LoginResponse>> test(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success(loginResponse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "INVALID_CREDENTIALS", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "INTERNAL_SERVER_ERROR", "An unexpected error occurred"));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API is running!");
    }
    
    @GetMapping("/register")
    public ResponseEntity<String> registerGet() {
        return ResponseEntity.ok("Register endpoint is accessible via GET - use POST for actual registration");
    }
    
    @GetMapping("/admin/list-user")
    public ResponseEntity<ListUserResponse> listUsers() {
        try {
            ListUserResponse response = userService.getAllUsers();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ListUserResponse(500, "00", "Internal server error", null));
        }
    }
    
    @PostMapping("/admin/update")
    public ResponseEntity<UpdateUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateRequest) {
        try {
            UpdateUserResponse response = userService.updateUserRoleAndStatus(updateRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new UpdateUserResponse(500, "00", "Internal server error"));
        }
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RegisterResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
        }
        return ResponseEntity.badRequest()
            .body(new RegisterResponse(400, errorMessage.toString(), null));
    }
}
