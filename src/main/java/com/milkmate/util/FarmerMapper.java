package com.milkmate.util;

import org.springframework.stereotype.Component;

import com.milkmate.dto.FarmerRequest;
import com.milkmate.dto.FarmerResponse;
import com.milkmate.entity.Farmer;

@Component
public class FarmerMapper {

    public Farmer toEntity(FarmerRequest request) {

        Farmer farmer = new Farmer();

        farmer.setFarmerCode(request.getFarmerCode());
        farmer.setFullName(request.getFullName());
        farmer.setMobile(request.getMobile());
        farmer.setVillage(request.getVillage());
        farmer.setAddress(request.getAddress());
        farmer.setActive(request.getActive());

        return farmer;
    }

    public FarmerResponse toResponse(Farmer farmer) {

        return new FarmerResponse(
                farmer.getId(),
                farmer.getFarmerCode(),
                farmer.getFullName(),
                farmer.getMobile(),
                farmer.getVillage(),
                farmer.getAddress(),
                farmer.getActive()
        );
    }
}