package com.milkmate.dto;

import java.math.BigDecimal;

public class FarmerDashboardResponse {

    private Long farmerId;
    private String farmerCode;
    private String farmerName;

    private BigDecimal todayMilk;
    private BigDecimal todayAmount;

    private BigDecimal totalPaid;
    private BigDecimal pendingAmount;

    private BigDecimal monthlyMilk;
    private BigDecimal monthlyAmount;

    public FarmerDashboardResponse() {
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

    public BigDecimal getTodayMilk() {
        return todayMilk;
    }

    public void setTodayMilk(BigDecimal todayMilk) {
        this.todayMilk = todayMilk;
    }

    public BigDecimal getTodayAmount() {
        return todayAmount;
    }

    public void setTodayAmount(BigDecimal todayAmount) {
        this.todayAmount = todayAmount;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(BigDecimal pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public BigDecimal getMonthlyMilk() {
        return monthlyMilk;
    }

    public void setMonthlyMilk(BigDecimal monthlyMilk) {
        this.monthlyMilk = monthlyMilk;
    }

    public BigDecimal getMonthlyAmount() {
        return monthlyAmount;
    }

    public void setMonthlyAmount(BigDecimal monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }
}