package com.milkmate.dto;

import com.milkmate.entity.Role;

public class RegisterResponse {

    private Long id;
    private Long farmerId;
    private String fullName;
    private String mobile;
    private String email;
    private Role role;
    private Boolean active;
    private String token;
    private String message;

    public RegisterResponse() {
    }

    public RegisterResponse(
            Long id,
            Long farmerId,
            String fullName,
            String mobile,
            String email,
            Role role,
            Boolean active,
            String token,
            String message) {

        this.id = id;
        this.farmerId = farmerId;
        this.fullName = fullName;
        this.mobile = mobile;
        this.email = email;
        this.role = role;
        this.active = active;
        this.token = token;
        this.message = message;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}