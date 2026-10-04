package com.milkmate.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.milkmate.service.SmsService;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${fast2sms.api-key}")
    private String apiKey;

    private final RestClient restClient;

    public SmsServiceImpl() {
        this.restClient = RestClient.builder()
                .baseUrl("https://www.fast2sms.com")
                .build();
    }

    @Override
    public boolean sendOtpSms(String mobile, String otp) {

        try {

            String message =
                    "Your MilkMate OTP is " + otp
                    + ". Do not share this OTP with anyone.";

            String response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/dev/bulkV2")
                            .queryParam("route", "q")
                            .queryParam("message", message)
                            .queryParam("numbers", mobile)
                            .build())
                    .header("Authorization", apiKey)
                    .retrieve()
                    .body(String.class);

            System.out.println("Fast2SMS Response: " + response);

            return true;

        } catch (Exception e) {

            System.out.println("Fast2SMS Error: " + e.getMessage());

            return false;
        }
    }
}