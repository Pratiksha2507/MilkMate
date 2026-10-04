package com.milkmate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milkmate.dto.FarmerDashboardResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.service.FarmerDashboardService;

@RestController
@RequestMapping("/api/farmer/dashboard")
public class FarmerDashboardController {

    private final FarmerDashboardService farmerDashboardService;
    private final FarmerRepository farmerRepository;

    public FarmerDashboardController(
            FarmerDashboardService farmerDashboardService,
            FarmerRepository farmerRepository) {

        this.farmerDashboardService = farmerDashboardService;
        this.farmerRepository = farmerRepository;
    }

    @GetMapping("/my")
    public ResponseEntity<FarmerDashboardResponse> getMyDashboard(
            Authentication authentication) {

        Farmer farmer =
                getLoggedInFarmer(authentication);

        return ResponseEntity.ok(
                farmerDashboardService
                        .getDashboard(farmer.getId()));
    }

    private Farmer getLoggedInFarmer(
            Authentication authentication) {

        if (authentication == null
                || authentication.getName() == null) {

            throw new ResourceNotFoundException(
                    "Farmer login not found");
        }

        return farmerRepository
                .findByMobile(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Farmer profile not linked with this login"));
    }
}