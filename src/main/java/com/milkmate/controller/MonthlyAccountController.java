package com.milkmate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.milkmate.dto.MonthlyAccountResponse;
import com.milkmate.entity.MonthlyAccount;
import com.milkmate.service.MonthlyAccountService;
import com.milkmate.util.MonthlyAccountMapper;

@RestController
@RequestMapping("/api/admin/monthly-accounts")
public class MonthlyAccountController {

    private final MonthlyAccountService monthlyAccountService;
    private final MonthlyAccountMapper monthlyAccountMapper;

    public MonthlyAccountController(
            MonthlyAccountService monthlyAccountService,
            MonthlyAccountMapper monthlyAccountMapper) {

        this.monthlyAccountService =
                monthlyAccountService;

        this.monthlyAccountMapper =
                monthlyAccountMapper;
    }


    // ==========================================
    // GENERATE MONTHLY ACCOUNT
    // ==========================================

    @PostMapping("/farmer/{farmerId}/generate")
    public ResponseEntity<MonthlyAccountResponse>
    generateMonthlyAccount(

            @PathVariable Long farmerId,

            @RequestParam Integer month,

            @RequestParam Integer year) {

        MonthlyAccount account =
                monthlyAccountService
                    .generateMonthlyAccount(
                        farmerId,
                        month,
                        year
                    );

        return ResponseEntity.ok(
                monthlyAccountMapper
                    .toResponse(account)
        );
    }


    // ==========================================
    // GET ALL MONTHLY ACCOUNTS
    // ==========================================

    @GetMapping
    public ResponseEntity<
            List<MonthlyAccountResponse>>
    getAllMonthlyAccounts() {

        List<MonthlyAccountResponse> response =
                monthlyAccountService
                    .getAllMonthlyAccounts()
                    .stream()
                    .map(
                        monthlyAccountMapper::toResponse
                    )
                    .toList();

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // GET MONTHLY ACCOUNT BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<MonthlyAccountResponse>
    getMonthlyAccountById(
            @PathVariable Long id) {

        MonthlyAccount account =
                monthlyAccountService
                    .getMonthlyAccountById(id);

        return ResponseEntity.ok(
                monthlyAccountMapper
                    .toResponse(account)
        );
    }


    // ==========================================
    // GET FARMER ACCOUNTS
    // ==========================================

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<
            List<MonthlyAccountResponse>>
    getAccountsByFarmer(
            @PathVariable Long farmerId) {

        List<MonthlyAccountResponse> response =
                monthlyAccountService
                    .getAccountsByFarmer(farmerId)
                    .stream()
                    .map(
                        monthlyAccountMapper::toResponse
                    )
                    .toList();

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // GET FARMER MONTHLY ACCOUNT
    // ==========================================

    @GetMapping("/farmer/{farmerId}/month")
    public ResponseEntity<MonthlyAccountResponse>
    getMonthlyAccount(

            @PathVariable Long farmerId,

            @RequestParam Integer month,

            @RequestParam Integer year) {

        MonthlyAccount account =
                monthlyAccountService
                    .getMonthlyAccount(
                        farmerId,
                        month,
                        year
                    );

        return ResponseEntity.ok(
                monthlyAccountMapper
                    .toResponse(account)
        );
    }


    // ==========================================
    // GET ACCOUNTS BY MONTH
    // ==========================================

    @GetMapping("/month")
    public ResponseEntity<
            List<MonthlyAccountResponse>>
    getAccountsByMonth(

            @RequestParam Integer month,

            @RequestParam Integer year) {

        List<MonthlyAccountResponse> response =
                monthlyAccountService
                    .getAccountsByMonth(
                        month,
                        year
                    )
                    .stream()
                    .map(
                        monthlyAccountMapper::toResponse
                    )
                    .toList();

        return ResponseEntity.ok(response);
    }
}