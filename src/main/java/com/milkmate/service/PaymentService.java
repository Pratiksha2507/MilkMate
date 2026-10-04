package com.milkmate.service;

import java.time.LocalDate;
import java.util.List;

import com.milkmate.entity.Payment;

public interface PaymentService {

    Payment createPayment(Long farmerId, Payment payment);

    Payment getPaymentById(Long id);

    List<Payment> getAllPayments();

    List<Payment> getPaymentsByFarmer(Long farmerId);

    List<Payment> getPaymentsByDateRange(
            LocalDate startDate,
            LocalDate endDate
    );

    List<Payment> getPaymentsByFarmerAndDateRange(
            Long farmerId,
            LocalDate startDate,
            LocalDate endDate
    );

    Payment updatePayment(Long id, Payment payment);

    void deletePayment(Long id);
}