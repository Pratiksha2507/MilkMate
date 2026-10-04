package com.milkmate.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MilkRateResponse {

    private Long id;
    private BigDecimal ratePerLiter;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Boolean active;

    public MilkRateResponse() {
    }

    public MilkRateResponse(
            Long id,
            BigDecimal ratePerLiter,
            LocalDate effectiveFrom,
            LocalDate effectiveTo,
            Boolean active) {

        this.id = id;
        this.ratePerLiter = ratePerLiter;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRatePerLiter() {
        return ratePerLiter;
    }

    public void setRatePerLiter(BigDecimal ratePerLiter) {
        this.ratePerLiter = ratePerLiter;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}