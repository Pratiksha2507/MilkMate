package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.entity.MilkRate;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.MilkRateRepository;
import com.milkmate.service.MilkRateService;

@Service
public class MilkRateServiceImpl implements MilkRateService {

    private final MilkRateRepository milkRateRepository;

    public MilkRateServiceImpl(
            MilkRateRepository milkRateRepository) {

        this.milkRateRepository =
                milkRateRepository;
    }

    @Override
    public MilkRate createRate(MilkRate milkRate) {

        validateRate(milkRate);

        LocalDate effectiveFrom =
                milkRate.getEffectiveFrom();

        MilkRate existing =
                milkRateRepository
                        .findByEffectiveFrom(
                                effectiveFrom
                        )
                        .orElse(null);

        if (existing != null) {

            throw new IllegalArgumentException(
                    "Milk rate already exists for effective date: "
                            + effectiveFrom
            );
        }

        if (milkRate.getActive() == null) {
            milkRate.setActive(true);
        }

        // Close older active rates
        if (Boolean.TRUE.equals(
                milkRate.getActive())) {

            List<MilkRate> activeRates =
                    milkRateRepository
                            .findByActiveTrueOrderByEffectiveFromDesc();

            for (MilkRate rate : activeRates) {

                if (rate.getEffectiveFrom()
                        .isBefore(effectiveFrom)) {

                    rate.setEffectiveTo(
                            effectiveFrom.minusDays(1)
                    );

                    rate.setActive(false);
                }
            }

            milkRateRepository.saveAll(
                    activeRates
            );
        }

        return milkRateRepository.save(
                milkRate
        );
    }


    @Override
    public MilkRate getRateById(Long id) {

        return milkRateRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Milk rate not found with id: "
                                        + id
                        )
                );
    }


    @Override
    public List<MilkRate> getAllRates() {

        return milkRateRepository
                .findByOrderByEffectiveFromDesc();
    }


    @Override
    public List<MilkRate> getActiveRates() {

        return milkRateRepository
                .findByActiveTrueOrderByEffectiveFromDesc();
    }


    @Override
    public MilkRate getApplicableRate(
            LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        List<MilkRate> rates =
                milkRateRepository
                        .findApplicableRates(date);

        if (rates.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No milk rate found for date: "
                            + date
            );
        }

        return rates.get(0);
    }


    @Override
    public MilkRate updateRate(
            Long id,
            MilkRate milkRate) {

        MilkRate existing =
                getRateById(id);

        validateRate(milkRate);

        MilkRate duplicate =
                milkRateRepository
                        .findByEffectiveFrom(
                                milkRate.getEffectiveFrom()
                        )
                        .orElse(null);

        if (duplicate != null
                && !duplicate.getId()
                        .equals(existing.getId())) {

            throw new IllegalArgumentException(
                    "Milk rate already exists for effective date: "
                            + milkRate.getEffectiveFrom()
            );
        }

        existing.setRatePerLiter(
                milkRate.getRatePerLiter()
        );

        existing.setEffectiveFrom(
                milkRate.getEffectiveFrom()
        );

        existing.setEffectiveTo(
                milkRate.getEffectiveTo()
        );

        existing.setActive(
                milkRate.getActive()
        );

        return milkRateRepository.save(
                existing
        );
    }


    @Override
    public void deleteRate(Long id) {

        if (!milkRateRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "Milk rate not found with id: "
                            + id
            );
        }

        milkRateRepository.deleteById(id);
    }


    private void validateRate(
            MilkRate milkRate) {

        if (milkRate == null) {

            throw new IllegalArgumentException(
                    "Milk rate data is required"
            );
        }

        if (milkRate.getRatePerLiter() == null
                || milkRate.getRatePerLiter()
                        .compareTo(
                                BigDecimal.ZERO
                        ) <= 0) {

            throw new IllegalArgumentException(
                    "Rate per liter must be greater than 0"
            );
        }

        if (milkRate.getEffectiveFrom() == null) {

            throw new IllegalArgumentException(
                    "Effective from date is required"
            );
        }

        if (milkRate.getEffectiveTo() != null
                && milkRate.getEffectiveTo()
                        .isBefore(
                                milkRate.getEffectiveFrom()
                        )) {

            throw new IllegalArgumentException(
                    "Effective to date cannot be before effective from date"
            );
        }
    }
}