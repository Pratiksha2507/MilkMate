package com.milkmate.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.milkmate.dto.OtpRequest;
import com.milkmate.dto.OtpVerifyRequest;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.User;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.security.JwtService;
import com.milkmate.service.OtpService;
import com.milkmate.service.SmsService;
import com.milkmate.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/otp")
public class OtpController {

    private final OtpService otpService;
    private final SmsService smsService;
    private final UserService userService;
    private final JwtService jwtService;
    private final FarmerRepository farmerRepository;

    public OtpController(
            OtpService otpService,
            SmsService smsService,
            UserService userService,
            JwtService jwtService,
            FarmerRepository farmerRepository) {

        this.otpService = otpService;
        this.smsService = smsService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.farmerRepository = farmerRepository;
    }

    // =========================
    // SEND OTP
    // =========================

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendOtp(
            @Valid @RequestBody OtpRequest request) {

        String mobile = request.getMobile();

        // Generate OTP
        String otp = otpService.generateOtp(mobile);

        // Send OTP through Fast2SMS
        boolean smsSent = smsService.sendOtpSms(mobile, otp);

        if (!smsSent) {

            return ResponseEntity.status(500).body(
                    Map.of(
                            "success", false,
                            "message", "Unable to send OTP SMS"
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "OTP sent successfully"
                )
        );
    }

    // =========================
    // VERIFY OTP
    // =========================

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @Valid @RequestBody OtpVerifyRequest request) {

        boolean verified = otpService.verifyOtp(
                request.getMobile(),
                request.getOtp()
        );

        if (!verified) {

            return ResponseEntity.status(401).body(
                    Map.of(
                            "success", false,
                            "message", "Invalid or expired OTP"
                    )
            );
        }

        User user = userService.findByMobile(
                request.getMobile()
        );

        // =========================
        // ADMIN LOGIN
        // =========================

        if ("9000000001".equals(request.getMobile())) {

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "success",
                    true
            );

            response.put(
                    "message",
                    "Admin OTP verified successfully"
            );

            response.put(
                    "userId",
                    user != null ? user.getId() : 1
            );

            response.put(
                    "fullName",
                    user != null
                            ? user.getFullName()
                            : "MilkMate Admin"
            );

            response.put(
                    "mobile",
                    "9000000001"
            );

            response.put(
                    "role",
                    "ADMIN"
            );

            // Generate JWT token
            if (user != null) {

                response.put(
                        "token",
                        jwtService.generateToken(user)
                );

            } else {

                response.put(
                        "token",
                        ""
                );
            }

            return ResponseEntity.ok(response);
        }

        // =========================
        // FARMER LOGIN
        // =========================

        if (user == null) {

            return ResponseEntity.status(404).body(
                    Map.of(
                            "success", false,
                            "message", "User not found"
                    )
            );
        }

        String token =
                jwtService.generateToken(user);

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "success",
                true
        );

        response.put(
                "message",
                "OTP verified successfully"
        );

        response.put(
                "userId",
                user.getId()
        );

        response.put(
                "fullName",
                user.getFullName()
        );

        response.put(
                "mobile",
                user.getMobile()
        );

        response.put(
                "role",
                user.getRole()
        );

        response.put(
                "token",
                token
        );

        // Farmer ID
        if (user.getRole() != null &&
                user.getRole().name().equals("FARMER")) {

            Farmer farmer =
                    farmerRepository
                            .findByMobile(
                                    user.getMobile()
                            )
                            .orElse(null);

            if (farmer != null) {

                response.put(
                        "farmerId",
                        farmer.getId()
                );
            }
        }

        return ResponseEntity.ok(response);
    }
}