package com.milkmate.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.service.MilkCollectionService;
import com.milkmate.util.MilkCollectionMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/farmer/milk-collections")
public class FarmerMilkCollectionController {

    private final MilkCollectionService milkCollectionService;
    private final MilkCollectionMapper milkCollectionMapper;
    private final FarmerRepository farmerRepository;

    public FarmerMilkCollectionController(
            MilkCollectionService milkCollectionService,
            MilkCollectionMapper milkCollectionMapper,
            FarmerRepository farmerRepository) {

        this.milkCollectionService = milkCollectionService;
        this.milkCollectionMapper = milkCollectionMapper;
        this.farmerRepository = farmerRepository;
    }

    @PostMapping("/my")
    public ResponseEntity<MilkCollectionResponse> createMyMilkCollection(
            @Valid @RequestBody MilkCollectionRequest request,
            Authentication authentication) {

        Farmer farmer =
                getLoggedInFarmer(authentication);

        MilkCollection milkCollection =
                milkCollectionMapper.toEntity(request);

        MilkCollection savedCollection =
                milkCollectionService.createMilkCollection(
                        farmer.getId(),
                        milkCollection);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        milkCollectionMapper
                                .toResponse(savedCollection)
                );
    }

    @GetMapping("/my")
    public ResponseEntity<List<MilkCollectionResponse>> getMyRecords(
            Authentication authentication) {

        Farmer farmer =
                getLoggedInFarmer(authentication);

        List<MilkCollectionResponse> response =
                milkCollectionService
                        .getCollectionsByFarmer(
                                farmer.getId())
                        .stream()
                        .map(milkCollectionMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/date-range")
    public ResponseEntity<List<MilkCollectionResponse>>
    getMyRecordsByDateRange(
            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            Authentication authentication) {

        Farmer farmer =
                getLoggedInFarmer(authentication);

        List<MilkCollectionResponse> response =
                milkCollectionService
                        .getCollectionsByFarmerAndDateRange(
                                farmer.getId(),
                                startDate,
                                endDate)
                        .stream()
                        .map(milkCollectionMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/record/{id}")
    public ResponseEntity<MilkCollectionResponse> getRecordById(
            @PathVariable Long id,
            Authentication authentication) {

        Farmer farmer =
                getLoggedInFarmer(authentication);

        MilkCollection collection =
                milkCollectionService
                        .getMilkCollectionById(id);

        ensureCollectionOwner(
                farmer,
                collection);

        return ResponseEntity.ok(
                milkCollectionMapper
                        .toResponse(collection));
    }

    @PutMapping("/record/{id}")
    public ResponseEntity<MilkCollectionResponse> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody MilkCollectionRequest request,
            Authentication authentication) {

        Farmer farmer =
                getLoggedInFarmer(authentication);

        MilkCollection existing =
                milkCollectionService
                        .getMilkCollectionById(id);

        ensureCollectionOwner(
                farmer,
                existing);

        MilkCollection milkCollection =
                milkCollectionMapper.toEntity(request);

        MilkCollection updatedCollection =
                milkCollectionService
                        .updateMilkCollection(
                                id,
                                milkCollection);

        return ResponseEntity.ok(
                milkCollectionMapper
                        .toResponse(updatedCollection));
    }

    @DeleteMapping("/record/{id}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long id,
            Authentication authentication) {

        Farmer farmer =
                getLoggedInFarmer(authentication);

        MilkCollection existing =
                milkCollectionService
                        .getMilkCollectionById(id);

        ensureCollectionOwner(
                farmer,
                existing);

        milkCollectionService
                .deleteMilkCollection(id);

        return ResponseEntity.noContent().build();
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

    private void ensureCollectionOwner(
            Farmer loggedInFarmer,
            MilkCollection collection) {

        if (collection.getFarmer() == null
                || !loggedInFarmer.getId().equals(
                        collection.getFarmer().getId())) {

            throw new IllegalArgumentException(
                    "You can access only your own milk records");
        }
    }
}