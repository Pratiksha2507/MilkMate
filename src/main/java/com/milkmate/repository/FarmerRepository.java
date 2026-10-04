package com.milkmate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milkmate.entity.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByFarmerCode(String farmerCode);

    Optional<Farmer> findByMobile(String mobile);

    boolean existsByFarmerCode(String farmerCode);

    boolean existsByMobile(String mobile);
}