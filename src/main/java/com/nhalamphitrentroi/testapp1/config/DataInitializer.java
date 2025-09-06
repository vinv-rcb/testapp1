package com.nhalamphitrentroi.testapp1.config;

import java.time.LocalDate;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nhalamphitrentroi.testapp1.entity.Database;
import com.nhalamphitrentroi.testapp1.entity.User;
import com.nhalamphitrentroi.testapp1.repository.DatabaseRepository;
import com.nhalamphitrentroi.testapp1.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DatabaseRepository databaseRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("=== DATA INITIALIZER STARTED ===");
        logger.info("Initializing database with default data...");
        
        // Create sample users for testing
        logger.info("Checking user data...");
        if (userRepository.count() == 0) {
            logger.info("No users found. Creating default users...");
            User admin = new User(
                "Manager",
                "IT Department",
                "admin",
                passwordEncoder.encode("admin123"),
                "Admin User",
                LocalDate.of(2023, 1, 15),
                "0123456789",
                "admin@company.com",
                "ADMIN",
                "ACTIVE"
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
                "USER",
                "ACTIVE"
            );
            
            User rAdmin = new User(
                "System Administrator",
                "IT Department",
                "r_admin",
                passwordEncoder.encode("radmin123"),
                "R Admin User",
                LocalDate.of(2023, 1, 1),
                "0111111111",
                "radmin@company.com",
                "R_ADMIN",
                "ACTIVE"
            );
            
            userRepository.save(admin);
            userRepository.save(employee);
            userRepository.save(rAdmin);
            
            logger.info("Successfully created 3 default users:");
            logger.info("- Admin: admin / admin123");
            logger.info("- Employee: employee / employee123");
            logger.info("- R Admin: r_admin / radmin123");
        } else {
            logger.info("Users already exist. Skipping user creation. Count: {}", userRepository.count());
        }
        
        // Initialize default database entries
        logger.info("Checking database entries...");
        if (databaseRepository.count() == 0) {
            logger.info("No database entries found. Creating default database entries...");
            
            Database t24vn = new Database("T24VN", "T24 VN");
            Database way4 = new Database("WAY4", "Database hệ thống way4");
            Database micro = new Database("MICRO", "Database hệ thống microservice");
            Database sale = new Database("SALE", "Database hệ thống sale");
            Database biz = new Database("BIZ", "Database hệ thống Biz");
            Database ebank = new Database("EBANK", "Database hệ thống ebanking");
            
            databaseRepository.save(t24vn);
            databaseRepository.save(way4);
            databaseRepository.save(micro);
            databaseRepository.save(sale);
            databaseRepository.save(biz);
            databaseRepository.save(ebank);
            
            logger.info("Successfully created 6 default database entries:");
            logger.info("- T24VN: T24 VN");
            logger.info("- WAY4: Database hệ thống way4");
            logger.info("- MICRO: Database hệ thống microservice");
            logger.info("- SALE: Database hệ thống sale");
            logger.info("- BIZ: Database hệ thống Biz");
            logger.info("- EBANK: Database hệ thống ebanking");
        } else {
            logger.info("Database entries already exist. Skipping database creation. Count: {}", databaseRepository.count());
        }
        
        logger.info("=== DATA INITIALIZER COMPLETED ===");
    }
}
