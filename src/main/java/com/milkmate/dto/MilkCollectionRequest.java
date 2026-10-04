package com.milkmate.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class MilkCollectionRequest {

    @NotNull(message = "Collection date is required")
    private LocalDate collectionDate;

    @NotBlank(message = "Session is required")
    @Pattern(
        regexp = "MORNING|EVENING",
        message = "Session must be MORNING or EVENING"
    )
    private String session;

    @NotNull(message = "Quantity is required")
    @DecimalMin(
        value = "0.01",
        message = "Quantity must be greater than 0"
    )
    private BigDecimal quantity;

    @DecimalMin(
        value = "0.01",
        message = "Fat must be greater than 0"
    )
    private BigDecimal fat;

    @DecimalMin(
        value = "0.01",
        message = "SNF must be greater than 0"
    )
    private BigDecimal snf;

    /*
     * Rate is NOT required from frontend.
     *
     * Backend will automatically get the applicable
     * milk rate using MilkRateService based on collection date.
     */

    public MilkCollectionRequest() {
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
}