package com.nhalamphitrentroi.testapp1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nhalamphitrentroi.testapp1.dto.ListUserResponse;
import com.nhalamphitrentroi.testapp1.dto.LoginRequest;
import com.nhalamphitrentroi.testapp1.dto.LoginResponse;
import com.nhalamphitrentroi.testapp1.dto.RegisterRequest;
import com.nhalamphitrentroi.testapp1.dto.RegisterResponse;
import com.nhalamphitrentroi.testapp1.dto.UpdateUserRequest;
import com.nhalamphitrentroi.testapp1.dto.UpdateUserResponse;
import com.nhalamphitrentroi.testapp1.dto.UserInfoDto;
import com.nhalamphitrentroi.testapp1.entity.User;
import com.nhalamphitrentroi.testapp1.repository.UserRepository;
import com.nhalamphitrentroi.testapp1.util.JwtUtil;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOptional.isEmpty()) {
            logger.warn("Login failed - Username not found: {}", loginRequest.getUsername());
            throw new RuntimeException("Invalid username or password");
        }
        
        User user = userOptional.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Login failed - Invalid password for username: {}", loginRequest.getUsername());
            throw new RuntimeException("Invalid username or password");
        }
        
        String token = jwtUtil.generateToken(user.getUsername());
        
        // Log successful login
        logger.info("User login successful - Username: {}, Name: {}, Role: {}, Status: {}", 
                   user.getUsername(), user.getName(), user.getRole(), user.getStatus());
        
        return new LoginResponse(
            token,
            user.getUsername(),
            user.getName(),
            user.getJoinDate(),
            user.getPhoneNumber(),
            user.getEmail(),
            user.getRole()
        );
    }
    
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public RegisterResponse register(RegisterRequest registerRequest) {
        logger.info("Starting registration for username: {}", registerRequest.getUsername());
        
        // Check if username already exists
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            logger.warn("Registration failed - Username already exists: {}", registerRequest.getUsername());
            return new RegisterResponse(500, "Tên người dùng đã tồn tại", null);
        }
        
        // Check if email already exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            logger.warn("Registration failed - Email already exists: {}", registerRequest.getEmail());
            return new RegisterResponse(500, "Email đã tồn tại", null);
        }
        
        // Create new user with PENDING status and null role
        logger.info("Creating new user object");
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.setName(registerRequest.getUsername()); // Use username as name for now
        newUser.setStatus("PENDING");
        newUser.setRole(null);
        newUser.setJoinDate(LocalDate.now());
        newUser.setTitle(registerRequest.getTitle() != null ? registerRequest.getTitle() : "");
        newUser.setDepartment(registerRequest.getDepartment() != null ? registerRequest.getDepartment() : "");
        newUser.setPhoneNumber(registerRequest.getPhoneNumber() != null ? registerRequest.getPhoneNumber() : "");
        
        logger.info("Saving user to database");
        userRepository.save(newUser);
        logger.info("User saved successfully with ID: {}", newUser.getId());
        
        // Log successful registration
        logger.info("User registration successful - Username: {}, Email: {}, Phone: {}, Status: PENDING", 
                   newUser.getUsername(), newUser.getEmail(), newUser.getPhoneNumber());
        
        return new RegisterResponse(200, "00", null);
    }
    
    public ListUserResponse getAllUsersExceptAdmin() {
        try {
            logger.info("Fetching all users except R_ADMIN role");
            List<User> users = userRepository.findByRoleNot("R_ADMIN");
            
            List<UserInfoDto> userInfoList = users.stream()
                .map(user -> new UserInfoDto(
                    user.getUsername(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getRole(),
                    user.getStatus()
                ))
                .toList();
            
            logger.info("Successfully fetched {} users", userInfoList.size());
            return new ListUserResponse(200, "00", null, userInfoList);
            
        } catch (Exception e) {
            logger.error("Error fetching users: {}", e.getMessage(), e);
            return new ListUserResponse(400, "00", "Lấy danh sách user không thành công", null);
        }
    }
    
    public ListUserResponse getAllUsers() {
        try {
            logger.info("Fetching all users with all statuses");
            List<User> users = userRepository.findAll();
            
            List<UserInfoDto> userInfoList = users.stream()
                    .filter(item -> !item.getRole().equals("ADMIN"))
                .map(user -> new UserInfoDto(
                    user.getUsername(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getRole(),
                    user.getStatus()
                ))
                .toList();
            
            logger.info("Successfully fetched {} users with all statuses", userInfoList.size());
            return new ListUserResponse(200, "00", null, userInfoList);
            
        } catch (Exception e) {
            logger.error("Error fetching all users: {}", e.getMessage(), e);
            return new ListUserResponse(400, "00", "Lấy danh sách user không thành công", null);
        }
    }
    
    public UpdateUserResponse updateUserRoleAndStatus(UpdateUserRequest updateRequest) {
        try {
            logger.info("Updating user role and status for username: {}", updateRequest.getUsername());
            
            Optional<User> userOptional = userRepository.findByUsername(updateRequest.getUsername());
            if (userOptional.isEmpty()) {
                logger.warn("User not found: {}", updateRequest.getUsername());
                return new UpdateUserResponse(500, "00", "Cập nhật người dùng không thành công");
            }
            
            User user = userOptional.get();
            
            // Update role if provided
            if (updateRequest.getRole() != null) {
                user.setRole(updateRequest.getRole());
                logger.info("Updated role to: {}", updateRequest.getRole());
            }
            
            // Update status if provided
            if (updateRequest.getStatus() != null) {
                user.setStatus(updateRequest.getStatus());
                logger.info("Updated status to: {}", updateRequest.getStatus());
            }
            
            userRepository.save(user);
            logger.info("Successfully updated user: {}", updateRequest.getUsername());
            
            return new UpdateUserResponse(200, "00", null);
            
        } catch (Exception e) {
            logger.error("Error updating user: {}", e.getMessage(), e);
            return new UpdateUserResponse(500, "00", "Cập nhật người dùng không thành công");
        }
    }
}
