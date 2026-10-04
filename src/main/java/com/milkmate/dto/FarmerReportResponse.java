package com.milkmate.dto;

import java.math.BigDecimal;

public class FarmerReportResponse {

    private Long farmerId;
    private String farmerCode;
    private String farmerName;

    private String startDate;
    private String endDate;

    private BigDecimal totalMilk;
    private BigDecimal totalAmount;

    private BigDecimal morningMilk;
    private BigDecimal eveningMilk;

    private long totalCollections;

    public FarmerReportResponse() {
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalMilk() {
        return totalMilk;
    }

    public void setTotalMilk(BigDecimal totalMilk) {
        this.totalMilk = totalMilk;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public long getTotalCollections() {
        return totalCollections;
    }

    public void setTotalCollections(long totalCollections) {
        this.totalCollections = totalCollections;
    }
}