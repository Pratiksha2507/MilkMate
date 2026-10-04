package com.milkmate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milkmate.entity.Farmer;
import com.milkmate.entity.Payment;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.service.PaymentService;

@RestController
@RequestMapping("/api/farmer/payments")
public class FarmerPaymentController {

    private final PaymentService paymentService;
    private final FarmerRepository farmerRepository;

    public FarmerPaymentController(
            PaymentService paymentService,
            FarmerRepository farmerRepository) {

        this.paymentService = paymentService;
        this.farmerRepository = farmerRepository;
    }

    @GetMapping
    public ResponseEntity<?> getMyPayments(
            Authentication authentication) {

        if (authentication == null ||
                authentication.getName() == null) {

            return ResponseEntity
                    .status(401)
                    .body("Unauthorized");
        }

        String mobile =
                authentication.getName();

        Farmer farmer =
                farmerRepository
                        .findByMobile(mobile)
                        .orElse(null);

        if (farmer == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        List<Payment> payments =
                paymentService
                        .getPaymentsByFarmer(
                                farmer.getId()
                        );

        return ResponseEntity.ok(
                payments
        );
    }
}