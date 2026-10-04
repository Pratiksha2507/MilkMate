package com.milkmate.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;

public interface MilkCollectionRepository
        extends JpaRepository<MilkCollection, Long> {

    // Get all collections of a farmer
    List<MilkCollection> findByFarmer(Farmer farmer);


    // Get farmer collections between two dates
    List<MilkCollection> findByFarmerAndCollectionDateBetween(
            Farmer farmer,
            LocalDate startDate,
            LocalDate endDate
    );


    // Get farmer collections by farmer ID between two dates
    List<MilkCollection> findByFarmerIdAndCollectionDateBetween(
            Long farmerId,
            LocalDate startDate,
            LocalDate endDate
    );


    // Find collection by farmer, date and session
    Optional<MilkCollection> findByFarmerAndCollectionDateAndSession(
            Farmer farmer,
            LocalDate collectionDate,
            String session
    );


    // Check duplicate collection
    boolean existsByFarmerAndCollectionDateAndSession(
            Farmer farmer,
            LocalDate collectionDate,
            String session
    );


    // Get collections for a specific date
    List<MilkCollection> findByCollectionDate(
            LocalDate collectionDate
    );


    // Get ALL farmers' collections between two dates
    List<MilkCollection> findByCollectionDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

}