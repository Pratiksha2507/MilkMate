package com.milkmate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.milkmate.dto.FarmerRequest;
import com.milkmate.dto.FarmerResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.service.FarmerService;
import com.milkmate.util.FarmerMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/farmers")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class FarmerController {

    private final FarmerService farmerService;
    private final FarmerMapper farmerMapper;

    public FarmerController(
            FarmerService farmerService,
            FarmerMapper farmerMapper) {

        this.farmerService = farmerService;
        this.farmerMapper = farmerMapper;
    }


    // ==========================================
    // CREATE FARMER
    // ==========================================

    @PostMapping
    public ResponseEntity<FarmerResponse> createFarmer(
            @Valid @RequestBody FarmerRequest request) {

        Farmer farmer =
                farmerMapper.toEntity(request);

        Farmer savedFarmer =
                farmerService.createFarmer(farmer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        farmerMapper.toResponse(
                                savedFarmer
                        )
                );
    }


    // ==========================================
    // GET ALL FARMERS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<FarmerResponse>>
    getAllFarmers() {

        List<FarmerResponse> response =
                farmerService
                        .getAllFarmers()
                        .stream()
                        .map(farmerMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // GET FARMER BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<FarmerResponse>
    getFarmerById(
            @PathVariable Long id) {

        Farmer farmer =
                farmerService.getFarmerById(id);

        return ResponseEntity.ok(
                farmerMapper.toResponse(
                        farmer
                )
        );
    }


    // ==========================================
    // GET FARMER BY CODE
    // ==========================================

    @GetMapping("/code/{farmerCode}")
    public ResponseEntity<FarmerResponse>
    getFarmerByCode(
            @PathVariable String farmerCode) {

        Farmer farmer =
                farmerService.getFarmerByCode(
                        farmerCode
                );

        return ResponseEntity.ok(
                farmerMapper.toResponse(
                        farmer
                )
        );
    }


    // ==========================================
    // GET FARMER BY MOBILE
    // ==========================================

    @GetMapping("/mobile/{mobile}")
    public ResponseEntity<FarmerResponse>
    getFarmerByMobile(
            @PathVariable String mobile) {

        Farmer farmer =
                farmerService.getFarmerByMobile(
                        mobile
                );

        return ResponseEntity.ok(
                farmerMapper.toResponse(
                        farmer
                )
        );
    }


    // ==========================================
    // UPDATE FARMER
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<FarmerResponse>
    updateFarmer(
            @PathVariable Long id,
            @Valid @RequestBody FarmerRequest request) {

        Farmer farmer =
                farmerMapper.toEntity(request);

        Farmer updatedFarmer =
                farmerService.updateFarmer(
                        id,
                        farmer
                );

        return ResponseEntity.ok(
                farmerMapper.toResponse(
                        updatedFarmer
                )
        );
    }


    // ==========================================
    // DELETE FARMER
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteFarmer(
            @PathVariable Long id) {

        farmerService.deleteFarmer(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}