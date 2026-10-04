package com.milkmate.dto;

import java.math.BigDecimal;

public class FarmerAnalyticsResponse {

    private Long farmerId;
    private String farmerCode;
    private String farmerName;

    private BigDecimal totalMilk;
    private BigDecimal morningMilk;
    private BigDecimal eveningMilk;
    private BigDecimal totalAmount;

    private long totalCollections;

    public FarmerAnalyticsResponse() {
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public BigDecimal getTotalMilk() {
        return totalMilk;
    }

    public void setTotalMilk(BigDecimal totalMilk) {
        this.totalMilk = totalMilk;
    }

    public BigDecimal getMorningMilk() {
        return morningMilk;
    }

    public void setMorningMilk(BigDecimal morningMilk) {
        this.morningMilk = morningMilk;
    }

    public BigDecimal getEveningMilk() {
        return eveningMilk;
    }

    public void setEveningMilk(BigDecimal eveningMilk) {
        this.eveningMilk = eveningMilk;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getTotalCollections() {
        return totalCollections;
    }

    public void setTotalCollections(long totalCollections) {
        this.totalCollections = totalCollections;
    }
}