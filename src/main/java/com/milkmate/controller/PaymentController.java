package com.milkmate.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.milkmate.entity.Payment;
import com.milkmate.service.PaymentService;

@RestController
@RequestMapping("/api/admin/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService =
                paymentService;
    }


    // ==========================================
    // CREATE PAYMENT
    // ==========================================

    @PostMapping("/farmer/{farmerId}")
    public ResponseEntity<Payment> createPayment(
            @PathVariable Long farmerId,
            @RequestBody Payment payment) {

        Payment savedPayment =
                paymentService.createPayment(
                        farmerId,
                        payment
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPayment);
    }


    // ==========================================
    // GET ALL PAYMENTS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Payment>>
    getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }


    // ==========================================
    // GET PAYMENT BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Payment>
    getPaymentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }


    // ==========================================
    // GET PAYMENTS BY FARMER
    // ==========================================

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Payment>>
    getPaymentsByFarmer(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByFarmer(
                        farmerId
                )
        );
    }


    // ==========================================
    // GET PAYMENTS BY DATE RANGE
    // ==========================================

    @GetMapping("/date-range")
    public ResponseEntity<List<Payment>>
    getPaymentsByDateRange(

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

        return ResponseEntity.ok(
                paymentService.getPaymentsByDateRange(
                        startDate,
                        endDate
                )
        );
    }


    // ==========================================
    // GET FARMER PAYMENTS BY DATE RANGE
    // ==========================================

    @GetMapping("/farmer/{farmerId}/date-range")
    public ResponseEntity<List<Payment>>
    getPaymentsByFarmerAndDateRange(

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

        return ResponseEntity.ok(
                paymentService
                    .getPaymentsByFarmerAndDateRange(
                        farmerId,
                        startDate,
                        endDate
                    )
        );
    }


    // ==========================================
    // UPDATE PAYMENT
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Payment>
    updatePayment(
            @PathVariable Long id,
            @RequestBody Payment payment) {

        return ResponseEntity.ok(
                paymentService.updatePayment(
                        id,
                        payment
                )
        );
    }


    // ==========================================
    // DELETE PAYMENT
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deletePayment(
            @PathVariable Long id) {

        paymentService.deletePayment(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}