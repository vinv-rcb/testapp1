package com.nhalamphitrentroi.testapp1.service;

import com.nhalamphitrentroi.testapp1.dto.LoginRequest;
import com.nhalamphitrentroi.testapp1.dto.LoginResponse;
import com.nhalamphitrentroi.testapp1.entity.User;
import com.nhalamphitrentroi.testapp1.repository.UserRepository;
import com.nhalamphitrentroi.testapp1.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }
        
        User user = userOptional.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        String token = jwtUtil.generateToken(user.getUsername());
        
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
}
