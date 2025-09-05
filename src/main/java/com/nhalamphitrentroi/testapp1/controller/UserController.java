package com.nhalamphitrentroi.testapp1.controller;

import com.nhalamphitrentroi.testapp1.dto.ApiResponse;
import com.nhalamphitrentroi.testapp1.dto.LoginRequest;
import com.nhalamphitrentroi.testapp1.dto.LoginResponse;
import com.nhalamphitrentroi.testapp1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
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

    @GetMapping("/test")
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
}
