package com.milkmate.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.milkmate.entity.MilkRate;

public interface MilkRateRepository
        extends JpaRepository<MilkRate, Long> {

    // =========================================
    // FIND RATE BY EFFECTIVE FROM DATE
    // =========================================

    Optional<MilkRate> findByEffectiveFrom(
            LocalDate effectiveFrom
    );

    // =========================================
    // GET ALL RATES - NEWEST FIRST
    // =========================================

    List<MilkRate> findByOrderByEffectiveFromDesc();

    // =========================================
    // GET ACTIVE RATES
    // =========================================

    List<MilkRate>
    findByActiveTrueOrderByEffectiveFromDesc();

    // =========================================
    // GET APPLICABLE RATE FOR A DATE
    // =========================================

    @Query("""
        SELECT m
        FROM MilkRate m
        WHERE m.active = true
        AND m.effectiveFrom <= :date
        AND (
            m.effectiveTo IS NULL
            OR m.effectiveTo >= :date
        )
        ORDER BY m.effectiveFrom DESC
        """)
    List<MilkRate> findApplicableRates(
            @Param("date") LocalDate date
    );
}