package com.milkmate.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.milkmate.dto.FarmerReportResponse;
import com.milkmate.dto.ReportResponse;
import com.milkmate.service.ReportService;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService) {

        this.reportService =
                reportService;
    }

    // ==========================================
    // DATE-WISE REPORT
    // ==========================================

    @GetMapping("/date-wise")
    public ResponseEntity<ReportResponse>
    getDateWiseReport(

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate date) {

        ReportResponse response =
                reportService.getDateWiseReport(
                        date
                );

        return ResponseEntity.ok(
                response
        );
    }

    // ==========================================
    // FARMER-WISE REPORT
    // ==========================================

    @GetMapping("/farmer-wise")
    public ResponseEntity<FarmerReportResponse>
    getFarmerWiseReport(

            @RequestParam Long farmerId,

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
                reportService.getFarmerWiseReport(
                        farmerId,
                        startDate,
                        endDate
                );

        return ResponseEntity.ok(
                response
        );
    }
}