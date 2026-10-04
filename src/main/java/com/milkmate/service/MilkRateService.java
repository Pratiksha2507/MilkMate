package com.milkmate.service;

import java.time.LocalDate;
import java.util.List;

import com.milkmate.entity.MilkRate;

public interface MilkRateService {

    MilkRate createRate(MilkRate milkRate);

    MilkRate getRateById(Long id);

    List<MilkRate> getAllRates();

    List<MilkRate> getActiveRates();

    MilkRate getApplicableRate(LocalDate date);

    MilkRate updateRate(Long id, MilkRate milkRate);

    void deleteRate(Long id);
}