package com.milkmate.dto;

public class FarmerResponse {

    private Long id;
    private String farmerCode;
    private String fullName;
    private String mobile;
    private String village;
    private String address;
    private Boolean active;

    public FarmerResponse() {
    }

    public FarmerResponse(Long id,
                          String farmerCode,
                          String fullName,
                          String mobile,
                          String village,
                          String address,
                          Boolean active) {
        this.id = id;
        this.farmerCode = farmerCode;
        this.fullName = fullName;
        this.mobile = mobile;
        this.village = village;
        this.address = address;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}