package com.milkmate.dto;

import com.milkmate.entity.Role;

public class LoginResponse {

    private String message;
    private Long userId;
    private Long farmerId;
    private String fullName;
    private String mobile;
    private Role role;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(
            String message,
            Long userId,
            Long farmerId,
            String fullName,
            String mobile,
            Role role,
            String token) {

        this.message = message;
        this.userId = userId;
        this.farmerId = farmerId;
        this.fullName = fullName;
        this.mobile = mobile;
        this.role = role;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}