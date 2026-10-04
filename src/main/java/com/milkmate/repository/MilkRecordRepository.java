package com.milkmate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.milkmate.entity.MilkRecord;

@Repository
public interface MilkRecordRepository extends JpaRepository<MilkRecord, Long> {

    List<MilkRecord> findByFarmerMobile(String mobile);

}