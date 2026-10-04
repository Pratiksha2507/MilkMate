package com.milkmate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milkmate.dto.FarmerAnalyticsResponse;
import com.milkmate.service.FarmerAnalyticsService;

@RestController
@RequestMapping("/api/admin/analytics")
@PreAuthorize("hasRole('ADMIN')")
public class FarmerAnalyticsController {

    private final FarmerAnalyticsService farmerAnalyticsService;

    public FarmerAnalyticsController(
            FarmerAnalyticsService farmerAnalyticsService) {

        this.farmerAnalyticsService =
                farmerAnalyticsService;
    }

    @GetMapping("/farmers")
    public ResponseEntity<List<FarmerAnalyticsResponse>>
    getFarmerAnalytics() {

        List<FarmerAnalyticsResponse> response =
                farmerAnalyticsService
                        .getFarmerAnalytics();

        return ResponseEntity.ok(response);
    }
}