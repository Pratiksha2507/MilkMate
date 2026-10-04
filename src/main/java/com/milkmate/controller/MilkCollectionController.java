package com.milkmate.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.milkmate.dto.MilkCollectionRequest;
import com.milkmate.dto.MilkCollectionResponse;
import com.milkmate.entity.MilkCollection;
import com.milkmate.service.MilkCollectionService;
import com.milkmate.util.MilkCollectionMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/milk-collections")
public class MilkCollectionController {

    private final MilkCollectionService milkCollectionService;
    private final MilkCollectionMapper milkCollectionMapper;

    public MilkCollectionController(
            MilkCollectionService milkCollectionService,
            MilkCollectionMapper milkCollectionMapper) {

        this.milkCollectionService = milkCollectionService;
        this.milkCollectionMapper = milkCollectionMapper;
    }

    // ================================
    // CREATE MILK COLLECTION
    // ================================

    @PostMapping("/farmer/{farmerId}")
    public ResponseEntity<MilkCollectionResponse> createMilkCollection(
            @PathVariable Long farmerId,
            @Valid @RequestBody MilkCollectionRequest request) {

        MilkCollection milkCollection =
                milkCollectionMapper.toEntity(request);

        MilkCollection savedCollection =
                milkCollectionService.createMilkCollection(
                        farmerId,
                        milkCollection
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        milkCollectionMapper.toResponse(
                                savedCollection
                        )
                );
    }

    // ================================
    // GET ALL MILK COLLECTIONS
    // ================================

    @GetMapping
    public ResponseEntity<List<MilkCollectionResponse>>
    getAllMilkCollections() {

        List<MilkCollectionResponse> response =
                milkCollectionService
                        .getAllMilkCollections()
                        .stream()
                        .map(milkCollectionMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // ================================
    // GET MILK COLLECTION BY ID
    // ================================

    @GetMapping("/{id}")
    public ResponseEntity<MilkCollectionResponse>
    getMilkCollectionById(
            @PathVariable Long id) {

        MilkCollection milkCollection =
                milkCollectionService
                        .getMilkCollectionById(id);

        return ResponseEntity.ok(
                milkCollectionMapper.toResponse(
                        milkCollection
                )
        );
    }

    // ================================
    // GET FARMER COLLECTIONS
    // ================================

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<MilkCollectionResponse>>
    getCollectionsByFarmer(
            @PathVariable Long farmerId) {

        List<MilkCollectionResponse> response =
                milkCollectionService
                        .getCollectionsByFarmer(farmerId)
                        .stream()
                        .map(milkCollectionMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // ================================
    // GET FARMER COLLECTIONS BY DATE
    // ================================

    @GetMapping("/farmer/{farmerId}/date-range")
    public ResponseEntity<List<MilkCollectionResponse>>
    getCollectionsByFarmerAndDateRange(
            @PathVariable Long farmerId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        List<MilkCollectionResponse> response =
                milkCollectionService
                        .getCollectionsByFarmerAndDateRange(
                                farmerId,
                                startDate,
                                endDate
                        )
                        .stream()
                        .map(milkCollectionMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    // ================================
    // UPDATE MILK COLLECTION
    // ================================

    @PutMapping("/{id}")
    public ResponseEntity<MilkCollectionResponse>
    updateMilkCollection(
            @PathVariable Long id,
            @Valid @RequestBody MilkCollectionRequest request) {

        MilkCollection milkCollection =
                milkCollectionMapper.toEntity(request);

        MilkCollection updatedCollection =
                milkCollectionService
                        .updateMilkCollection(
                                id,
                                milkCollection
                        );

        return ResponseEntity.ok(
                milkCollectionMapper.toResponse(
                        updatedCollection
                )
        );
    }

    // ================================
    // DELETE MILK COLLECTION
    // ================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMilkCollection(
            @PathVariable Long id) {

        milkCollectionService
                .deleteMilkCollection(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}