package com.milkmate.dto;

import java.math.BigDecimal;

public class DateWiseAnalyticsResponse {

    private String startDate;
    private String endDate;

    private BigDecimal totalMilk;
    private BigDecimal morningMilk;
    private BigDecimal eveningMilk;

    private BigDecimal totalAmount;

    private long totalCollections;

    public DateWiseAnalyticsResponse() {
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