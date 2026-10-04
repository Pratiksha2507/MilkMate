package com.milkmate.service.impl;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.milkmate.service.OtpService;

@Service
public class OtpServiceImpl implements OtpService {

    private final Map<String, String> otpStore =
            new ConcurrentHashMap<>();

    private final SecureRandom secureRandom =
            new SecureRandom();

    @Override
    public String generateOtp(String mobile) {

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1000000)
        );

        otpStore.put(mobile, otp);

        return otp;
    }

    @Override
    public boolean verifyOtp(String mobile, String otp) {

        String savedOtp = otpStore.get(mobile);

        if (savedOtp == null) {
            return false;
        }

        if (savedOtp.equals(otp)) {
            otpStore.remove(mobile);
            return true;
        }

        return false;
    }
}