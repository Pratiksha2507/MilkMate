package com.milkmate.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milkmate.dto.LoginRequest;
import com.milkmate.dto.LoginResponse;
import com.milkmate.dto.RegisterRequest;
import com.milkmate.dto.RegisterResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.Role;
import com.milkmate.entity.User;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.security.JwtService;
import com.milkmate.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final FarmerRepository farmerRepository;

    @Value("${milkmate.demo-login:false}")
    private boolean demoLoginEnabled;

    public AuthController(
            UserService userService,
            JwtService jwtService,
            FarmerRepository farmerRepository) {

        this.userService = userService;
        this.jwtService = jwtService;
        this.farmerRepository = farmerRepository;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        if (userService.findByMobile(
                request.getMobile()) != null) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && userService.findByEmail(
                        request.getEmail()) != null) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        User user = new User();

        user.setFullName(
                request.getFullName());

        user.setMobile(
                request.getMobile());

        user.setEmail(
                request.getEmail());

        user.setPassword(
                request.getPassword());

        User savedUser =
                userService.registerUser(user);

        Farmer farmer = new Farmer();

        farmer.setFarmerCode(
                generateFarmerCode());

        farmer.setFullName(
                request.getFullName());

        farmer.setMobile(
                request.getMobile());

        farmer.setVillage("");

        farmer.setAddress("");

        farmer.setActive(true);

        Farmer savedFarmer =
                farmerRepository.save(farmer);

        String token =
                jwtService.generateToken(savedUser);

        RegisterResponse response =
                new RegisterResponse(
                        savedUser.getId(),
                        savedFarmer.getId(),
                        savedUser.getFullName(),
                        savedUser.getMobile(),
                        savedUser.getEmail(),
                        savedUser.getRole(),
                        savedUser.getActive(),
                        token,
                        "Registration successful"
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    private String generateFarmerCode() {

        long number =
                farmerRepository.count() + 1;

        String farmerCode;

        do {

            farmerCode =
                    String.format(
                            "F%03d",
                            number);

            number++;

        } while (
                farmerRepository
                        .existsByFarmerCode(farmerCode)
        );

        return farmerCode;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        String mobile =
                request.getMobile().trim();

        User user =
                userService.authenticateUser(
                        mobile,
                        request.getPassword()
                );

        if (user == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new LoginResponse(
                                    "Invalid mobile number or password",
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                            )
                    );
        }

        Long farmerId = null;

        if (user.getRole() == Role.FARMER) {

            Farmer farmer =
                    farmerRepository
                            .findByMobile(
                                    user.getMobile())
                            .orElse(null);

            if (farmer == null
                    || !Boolean.TRUE.equals(
                            farmer.getActive())) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                                new LoginResponse(
                                        "Farmer profile is inactive or not linked",
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                )
                        );
            }

            farmerId = farmer.getId();
        }

        String token =
                jwtService.generateToken(user);

        LoginResponse response =
                new LoginResponse(
                        "Login successful",
                        user.getId(),
                        farmerId,
                        user.getFullName(),
                        user.getMobile(),
                        user.getRole(),
                        token
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/demo-admin-login")
    public ResponseEntity<LoginResponse> demoAdminLogin(
            @RequestBody LoginRequest request) {

        if (!demoLoginEnabled) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            new LoginResponse(
                                    "Demo login is disabled",
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                            )
                    );
        }

        User admin =
                userService.findByMobile(
                        "9000000001");

        if (admin == null) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new LoginResponse(
                                    "Demo admin account not found",
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                            )
                    );
        }

        admin.setRole(Role.ADMIN);
        admin.setActive(true);

        String token =
                jwtService.generateToken(admin);

        String enteredMobile =
                request.getMobile();

        return ResponseEntity.ok(
                new LoginResponse(
                        "Demo login successful",
                        admin.getId(),
                        null,
                        "MilkMate Demo User",
                        enteredMobile,
                        Role.ADMIN,
                        token
                )
        );
    }
}