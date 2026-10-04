package com.milkmate.service;

import java.util.List;

import com.milkmate.entity.Farmer;

public interface FarmerService {

    Farmer createFarmer(Farmer farmer);

    Farmer getFarmerById(Long id);

    Farmer getFarmerByCode(String farmerCode);

    Farmer getFarmerByMobile(String mobile);

    List<Farmer> getAllFarmers();

    Farmer updateFarmer(Long id, Farmer farmer);

    void deleteFarmer(Long id);
}