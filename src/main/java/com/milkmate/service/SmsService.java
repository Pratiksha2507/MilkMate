package com.milkmate.service;

public interface SmsService {

    boolean sendOtpSms(String mobile, String otp);
}