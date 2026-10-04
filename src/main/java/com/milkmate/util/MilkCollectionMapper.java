package com.milkmate.util;

import org.springframework.stereotype.Component;

import com.milkmate.dto.MilkCollectionRequest;
import com.milkmate.dto.MilkCollectionResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;

@Component
public class MilkCollectionMapper {

    // Request DTO → Entity
    public MilkCollection toEntity(MilkCollectionRequest request) {

        MilkCollection milkCollection = new MilkCollection();

        milkCollection.setCollectionDate(
                request.getCollectionDate()
        );

        milkCollection.setSession(
                request.getSession()
        );

        milkCollection.setQuantity(
                request.getQuantity()
        );

        milkCollection.setFat(
                request.getFat()
        );

        milkCollection.setSnf(
                request.getSnf()
        );

        /*
         * Rate is NOT taken from frontend.
         *
         * MilkCollectionService will automatically
         * get the applicable rate from MilkRateService.
         */

        return milkCollection;
    }

    // Entity → Response DTO
    public MilkCollectionResponse toResponse(
            MilkCollection milkCollection) {

        Farmer farmer = milkCollection.getFarmer();

        return new MilkCollectionResponse(
                milkCollection.getId(),
                farmer.getId(),
                farmer.getFarmerCode(),
                farmer.getFullName(),
                milkCollection.getCollectionDate(),
                milkCollection.getSession(),
                milkCollection.getQuantity(),
                milkCollection.getFat(),
                milkCollection.getSnf(),
                milkCollection.getRate(),
                milkCollection.getTotalAmount()
        );
    }
}