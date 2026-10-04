package com.milkmate.service;

public interface OtpService {

    String generateOtp(String mobile);

    boolean verifyOtp(String mobile, String otp);
}