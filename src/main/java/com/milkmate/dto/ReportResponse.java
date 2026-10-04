package com.milkmate.dto;

import java.math.BigDecimal;

public class ReportResponse {

    private String reportDate;
    private BigDecimal totalMilk;
    private BigDecimal totalAmount;
    private BigDecimal morningMilk;
    private BigDecimal eveningMilk;
    private long totalCollections;

    public ReportResponse() {
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
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