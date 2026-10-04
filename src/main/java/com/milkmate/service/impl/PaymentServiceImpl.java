package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.entity.Farmer;
import com.milkmate.entity.NotificationType;
import com.milkmate.entity.Payment;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.PaymentRepository;
import com.milkmate.service.NotificationService;
import com.milkmate.service.PaymentService;

@Service
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository
            paymentRepository;

    private final FarmerRepository
            farmerRepository;

    private final NotificationService
            notificationService;


    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            FarmerRepository farmerRepository,
            NotificationService notificationService) {

        this.paymentRepository =
                paymentRepository;

        this.farmerRepository =
                farmerRepository;

        this.notificationService =
                notificationService;
    }


    // ==========================================
    // CREATE PAYMENT
    // ==========================================

    @Override
    public Payment createPayment(
            Long farmerId,
            Payment payment) {

        Farmer farmer =
                farmerRepository
                        .findById(farmerId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Farmer not found with id: "
                                                + farmerId
                                )
                        );


        validatePayment(
                payment
        );


        payment.setFarmer(
                farmer
        );


        payment.setPaymentMode(
                payment.getPaymentMode()
                        .trim()
                        .toUpperCase()
        );


        Payment savedPayment =
                paymentRepository.save(
                        payment
                );


        String message =
                "₹"
                + savedPayment.getAmount()
                + " payment received successfully on "
                + savedPayment.getPaymentDate()
                + ".";


        notificationService.createNotification(
                "Payment Received",
                message,
                NotificationType.PAYMENT_RECEIVED,
                farmer.getId()
        );


        return savedPayment;
    }


    // ==========================================
    // GET BY ID
    // ==========================================

    @Override
    public Payment getPaymentById(
            Long id) {

        return paymentRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Payment not found with id: "
                                        + id
                        )
                );
    }


    // ==========================================
    // GET ALL
    // ==========================================

    @Override
    public List<Payment> getAllPayments() {

        return paymentRepository
                .findAll();
    }


    // ==========================================
    // GET BY FARMER
    // ==========================================

    @Override
    public List<Payment> getPaymentsByFarmer(
            Long farmerId) {

        if (
            !farmerRepository
                    .existsById(farmerId)
        ) {

            throw new ResourceNotFoundException(
                    "Farmer not found with id: "
                            + farmerId
            );
        }


        return paymentRepository
                .findByFarmerIdOrderByPaymentDateDesc(
                        farmerId
                );
    }


    // ==========================================
    // DATE RANGE
    // ==========================================

    @Override
    public List<Payment>
            getPaymentsByDateRange(
                    LocalDate startDate,
                    LocalDate endDate) {

        validateDateRange(
                startDate,
                endDate
        );


        return paymentRepository
                .findByPaymentDateBetween(
                        startDate,
                        endDate
                );
    }


    // ==========================================
    // FARMER + DATE RANGE
    // ==========================================

    @Override
    public List<Payment>
            getPaymentsByFarmerAndDateRange(
                    Long farmerId,
                    LocalDate startDate,
                    LocalDate endDate) {

        if (
            !farmerRepository
                    .existsById(farmerId)
        ) {

            throw new ResourceNotFoundException(
                    "Farmer not found with id: "
                            + farmerId
            );
        }


        validateDateRange(
                startDate,
                endDate
        );


        return paymentRepository
                .findByFarmerIdAndPaymentDateBetween(
                        farmerId,
                        startDate,
                        endDate
                );
    }


    // ==========================================
    // UPDATE
    // ==========================================

    @Override
    public Payment updatePayment(
            Long id,
            Payment payment) {

        Payment existing =
                paymentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Payment not found with id: "
                                                + id
                                )
                        );


        validatePayment(
                payment
        );


        existing.setPaymentDate(
                payment.getPaymentDate()
        );

        existing.setAmount(
                payment.getAmount()
        );

        existing.setPaymentMode(
                payment.getPaymentMode()
                        .trim()
                        .toUpperCase()
        );

        existing.setReferenceNumber(
                payment.getReferenceNumber()
        );

        existing.setNotes(
                payment.getNotes()
        );


        return paymentRepository.save(
                existing
        );
    }


    // ==========================================
    // DELETE
    // ==========================================

    @Override
    public void deletePayment(
            Long id) {

        if (
            !paymentRepository
                    .existsById(id)
        ) {

            throw new ResourceNotFoundException(
                    "Payment not found with id: "
                            + id
            );
        }


        paymentRepository.deleteById(
                id
        );
    }


    // ==========================================
    // PAYMENT VALIDATION
    // ==========================================

    private void validatePayment(
            Payment payment) {

        if (payment == null) {

            throw new IllegalArgumentException(
                    "Payment data is required"
            );
        }


        if (
            payment.getAmount() == null ||
            payment.getAmount()
                    .compareTo(
                            BigDecimal.ZERO
                    ) <= 0
        ) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than 0"
            );
        }


        if (
            payment.getPaymentMode()
                    == null ||
            payment.getPaymentMode()
                    .trim()
                    .isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "Payment mode is required"
            );
        }


        if (
            payment.getPaymentDate()
                    == null
        ) {

            throw new IllegalArgumentException(
                    "Payment date is required"
            );
        }
    }


    // ==========================================
    // DATE RANGE VALIDATION
    // ==========================================

    private void validateDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        if (
            startDate == null ||
            endDate == null
        ) {

            throw new IllegalArgumentException(
                    "Start date and end date are required"
            );
        }


        if (
            startDate.isAfter(endDate)
        ) {

            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }
    }
}