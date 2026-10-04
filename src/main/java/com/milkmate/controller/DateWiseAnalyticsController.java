package com.milkmate.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.milkmate.dto.DateWiseAnalyticsResponse;
import com.milkmate.service.DateWiseAnalyticsService;

@RestController
@RequestMapping("/api/admin/analytics")
@PreAuthorize("hasRole('ADMIN')")
public class DateWiseAnalyticsController {

    private final DateWiseAnalyticsService dateWiseAnalyticsService;

    public DateWiseAnalyticsController(
            DateWiseAnalyticsService dateWiseAnalyticsService) {

        this.dateWiseAnalyticsService =
                dateWiseAnalyticsService;
    }

    @GetMapping("/date-range")
    public ResponseEntity<DateWiseAnalyticsResponse>
    getDateWiseAnalytics(

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

        DateWiseAnalyticsResponse response =
                dateWiseAnalyticsService
                        .getDateWiseAnalytics(
                                startDate,
                                endDate
                        );

        return ResponseEntity.ok(response);
    }
}