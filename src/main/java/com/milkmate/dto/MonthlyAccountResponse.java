package com.milkmate.dto;

import java.math.BigDecimal;

public class MonthlyAccountResponse {

    private Long id;
    private Long farmerId;
    private String farmerCode;
    private String farmerName;

    private Integer accountMonth;
    private Integer accountYear;

    private BigDecimal totalMilk;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;

    private String status;

    public MonthlyAccountResponse() {
    }

    public MonthlyAccountResponse(
            Long id,
            Long farmerId,
            String farmerCode,
            String farmerName,
            Integer accountMonth,
            Integer accountYear,
            BigDecimal totalMilk,
            BigDecimal totalAmount,
            BigDecimal paidAmount,
            BigDecimal balanceAmount,
            String status) {

        this.id = id;
        this.farmerId = farmerId;
        this.farmerCode = farmerCode;
        this.farmerName = farmerName;
        this.accountMonth = accountMonth;
        this.accountYear = accountYear;
        this.totalMilk = totalMilk;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balanceAmount = balanceAmount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAccountMonth() {
        return accountMonth;
    }

    public void setAccountMonth(Integer accountMonth) {
        this.accountMonth = accountMonth;
    }

    public Integer getAccountYear() {
        return accountYear;
    }

    public void setAccountYear(Integer accountYear) {
        this.accountYear = accountYear;
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

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}