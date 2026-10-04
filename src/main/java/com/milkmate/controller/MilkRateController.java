package com.milkmate.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.milkmate.entity.MilkRate;
import com.milkmate.service.MilkRateService;

@RestController
@RequestMapping("/api")
public class MilkRateController {

    private final MilkRateService milkRateService;

    public MilkRateController(MilkRateService milkRateService) {
        this.milkRateService = milkRateService;
    }

    @PostMapping("/admin/milk-rates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MilkRate> createRate(
            @RequestBody MilkRate milkRate) {

        MilkRate savedRate =
                milkRateService.createRate(milkRate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedRate);
    }

    @GetMapping("/admin/milk-rates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MilkRate>> getAllRates() {

        return ResponseEntity.ok(
                milkRateService.getAllRates()
        );
    }

    @GetMapping("/admin/milk-rates/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MilkRate>> getActiveRates() {

        return ResponseEntity.ok(
                milkRateService.getActiveRates()
        );
    }

    @GetMapping("/milk-rates/applicable")
    public ResponseEntity<MilkRate> getApplicableRate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                milkRateService.getApplicableRate(date)
        );
    }

    @GetMapping("/admin/milk-rates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MilkRate> getRateById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                milkRateService.getRateById(id)
        );
    }

    @PutMapping("/admin/milk-rates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MilkRate> updateRate(
            @PathVariable Long id,
            @RequestBody MilkRate milkRate) {

        return ResponseEntity.ok(
                milkRateService.updateRate(
                        id,
                        milkRate
                )
        );
    }

    @DeleteMapping("/admin/milk-rates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRate(
            @PathVariable Long id) {

        milkRateService.deleteRate(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}