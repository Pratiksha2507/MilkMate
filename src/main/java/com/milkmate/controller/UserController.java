package com.milkmate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.milkmate.entity.User;
import com.milkmate.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository =
                userRepository;

        this.passwordEncoder =
                passwordEncoder;
    }

    // ==========================================
    // RESET PASSWORD - ADMIN ONLY
    // ==========================================

    @PutMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String>
    resetPassword(

            @RequestParam String mobile,

            @RequestParam String newPassword) {

        if (
            mobile == null ||
            mobile.trim().isEmpty()
        ) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Mobile number is required"
                    );
        }

        if (
            newPassword == null ||
            newPassword.length() < 8
        ) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Password must contain at least 8 characters"
                    );
        }

        User user =
                userRepository
                        .findByMobile(
                                mobile.trim()
                        )
                        .orElse(null);

        if (user == null) {

            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            "User not found"
                    );
        }

        user.setPassword(
                passwordEncoder.encode(
                        newPassword
                )
        );

        userRepository.save(user);

        return ResponseEntity.ok(
                "Password reset successfully"
        );
    }
}