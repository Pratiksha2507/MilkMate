package com.milkmate.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.milkmate.entity.Role;
import com.milkmate.entity.User;
import com.milkmate.repository.UserRepository;

@Configuration
public class AdminDataInitializer {

    @Bean
    CommandLineRunner createOrUpdateAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminMobile = "9000000001";
            String adminPassword = "Admin@12345";

            User admin =
                    userRepository
                            .findByMobile(adminMobile)
                            .orElse(null);

            if (admin == null) {

                admin = new User();

                admin.setFullName(
                        "MilkMate Demo Admin"
                );

                admin.setMobile(
                        adminMobile
                );

                admin.setEmail(
                        "admin@milkmate.com"
                );
            }

            admin.setPassword(
                    passwordEncoder.encode(
                            adminPassword
                    )
            );

            admin.setRole(
                    Role.ADMIN
            );

            admin.setActive(
                    true
            );

            userRepository.save(admin);

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "MILKMATE DEMO ADMIN READY"
            );

            System.out.println(
                    "Default Admin: " +
                    adminMobile
            );

            System.out.println(
                    "======================================"
            );
        };
    }
}