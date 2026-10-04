package com.milkmate.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milkmate.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByFarmerIdOrderByPaymentDateDesc(Long farmerId);

    List<Payment> findByPaymentDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    List<Payment> findByFarmerIdAndPaymentDateBetween(
            Long farmerId,
            LocalDate startDate,
            LocalDate endDate
    );
}