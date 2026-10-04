package com.milkmate.util;

import org.springframework.stereotype.Component;

import com.milkmate.dto.MilkRateRequest;
import com.milkmate.dto.MilkRateResponse;
import com.milkmate.entity.MilkRate;

@Component
public class MilkRateMapper {

    public MilkRate toEntity(MilkRateRequest request) {

        MilkRate milkRate = new MilkRate();

        milkRate.setRatePerLiter(request.getRatePerLiter());
        milkRate.setEffectiveFrom(request.getEffectiveFrom());
        milkRate.setEffectiveTo(request.getEffectiveTo());
        milkRate.setActive(request.getActive());

        return milkRate;
    }

    public MilkRateResponse toResponse(MilkRate milkRate) {

        return new MilkRateResponse(
            milkRate.getId(),
            milkRate.getRatePerLiter(),
            milkRate.getEffectiveFrom(),
            milkRate.getEffectiveTo(),
            milkRate.getActive()
        );
    }
}