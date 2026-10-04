package com.milkmate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.milkmate.entity.MilkRecord;
import com.milkmate.repository.MilkRecordRepository;

@RestController
@RequestMapping("/api/farmer/records")
@CrossOrigin
public class FarmerRecordController {

    private final MilkRecordRepository milkRecordRepository;

    public FarmerRecordController(
            MilkRecordRepository milkRecordRepository) {

        this.milkRecordRepository = milkRecordRepository;
    }

    @GetMapping("/mobile/{mobile}")
    public ResponseEntity<List<MilkRecord>> getFarmerRecords(
            @PathVariable String mobile) {

        List<MilkRecord> records =
                milkRecordRepository.findByFarmerMobile(mobile);

        return ResponseEntity.ok(records);
    }
}