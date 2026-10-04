package com.milkmate.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.milkmate.dto.FarmerReportResponse;
import com.milkmate.service.FarmerReportService;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class FarmerReportController {

    private final FarmerReportService farmerReportService;

    public FarmerReportController(
            FarmerReportService farmerReportService) {

        this.farmerReportService =
                farmerReportService;
    }


    // ==========================================
    // FARMER REPORT
    // ==========================================

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<FarmerReportResponse>
    getFarmerReport(

            @PathVariable Long farmerId,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate endDate) {

        FarmerReportResponse response =
                farmerReportService.getFarmerReport(
                        farmerId,
                        startDate,
                        endDate
                );

        return ResponseEntity.ok(response);
    }
}