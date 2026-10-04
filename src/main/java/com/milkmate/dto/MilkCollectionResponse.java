package com.milkmate.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MilkCollectionResponse {

    private Long id;

    private Long farmerId;

    private String farmerCode;

    private String farmerName;

    private LocalDate collectionDate;

    private String session;

    private BigDecimal quantity;

    private BigDecimal fat;

    private BigDecimal snf;

    private BigDecimal rate;

    private BigDecimal totalAmount;

    public MilkCollectionResponse() {
    }

    public MilkCollectionResponse(
            Long id,
            Long farmerId,
            String farmerCode,
            String farmerName,
            LocalDate collectionDate,
            String session,
            BigDecimal quantity,
            BigDecimal fat,
            BigDecimal snf,
            BigDecimal rate,
            BigDecimal totalAmount) {

        this.id = id;
        this.farmerId = farmerId;
        this.farmerCode = farmerCode;
        this.farmerName = farmerName;
        this.collectionDate = collectionDate;
        this.session = session;
        this.quantity = quantity;
        this.fat = fat;
        this.snf = snf;
        this.rate = rate;
        this.totalAmount = totalAmount;
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

    public LocalDate getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public BigDecimal getSnf() {
        return snf;
    }

    public void setSnf(BigDecimal snf) {
        this.snf = snf;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}