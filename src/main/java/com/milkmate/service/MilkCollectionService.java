package com.milkmate.service;

import java.time.LocalDate;
import java.util.List;

import com.milkmate.entity.MilkCollection;

public interface MilkCollectionService {

    MilkCollection createMilkCollection(
            Long farmerId,
            MilkCollection milkCollection
    );

    MilkCollection getMilkCollectionById(Long id);

    List<MilkCollection> getAllMilkCollections();

    List<MilkCollection> getCollectionsByFarmer(
            Long farmerId
    );

    List<MilkCollection> getCollectionsByFarmerAndDateRange(
            Long farmerId,
            LocalDate startDate,
            LocalDate endDate
    );

    MilkCollection updateMilkCollection(
            Long id,
            MilkCollection milkCollection
    );

    void deleteMilkCollection(Long id);
}