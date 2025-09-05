package com.nhalamphitrentroi.testapp1.config;

import com.nhalamphitrentroi.testapp1.entity.User;
import com.nhalamphitrentroi.testapp1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample users for testing
        if (userRepository.count() == 0) {
            User admin = new User(
                "Manager",
                "IT Department",
                "admin",
                passwordEncoder.encode("admin123"),
                "Admin User",
                LocalDate.of(2023, 1, 15),
                "0123456789",
                "admin@company.com",
                "ADMIN"
            );
            
            User employee = new User(
                "Developer",
                "IT Department",
                "employee",
                passwordEncoder.encode("employee123"),
                "Employee User",
                LocalDate.of(2023, 6, 1),
                "0987654321",
                "employee@company.com",
                "USER"
            );
            
            userRepository.save(admin);
            userRepository.save(employee);
            
            System.out.println("Sample users created:");
            System.out.println("Admin - Username: admin, Password: admin123");
            System.out.println("Employee - Username: employee, Password: employee123");
        }
    }
}
