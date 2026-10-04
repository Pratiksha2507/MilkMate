package com.milkmate.dto;

import java.math.BigDecimal;

public class AdminDashboardResponse {

    private long totalFarmers;
    private long activeFarmers;

    private BigDecimal todayMilk;
    private BigDecimal todayCollection;

    private BigDecimal morningMilk;
    private BigDecimal eveningMilk;

    private BigDecimal totalPaid;
    private BigDecimal pendingAmount;

    public AdminDashboardResponse() {
    }

    public long getTotalFarmers() {
        return totalFarmers;
    }

    public void setTotalFarmers(long totalFarmers) {
        this.totalFarmers = totalFarmers;
    }

    public long getActiveFarmers() {
        return activeFarmers;
    }

    public void setActiveFarmers(long activeFarmers) {
        this.activeFarmers = activeFarmers;
    }

    public BigDecimal getTodayMilk() {
        return todayMilk;
    }

    public void setTodayMilk(BigDecimal todayMilk) {
        this.todayMilk = todayMilk;
    }

    public BigDecimal getTodayCollection() {
        return todayCollection;
    }

    public void setTodayCollection(BigDecimal todayCollection) {
        this.todayCollection = todayCollection;
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
}