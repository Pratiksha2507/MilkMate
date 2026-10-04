package com.milkmate.util;

import org.springframework.stereotype.Component;

import com.milkmate.dto.PaymentRequest;
import com.milkmate.dto.PaymentResponse;
import com.milkmate.entity.Payment;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequest request) {

        Payment payment = new Payment();

        payment.setPaymentDate(request.getPaymentDate());
        payment.setAmount(request.getAmount());
        payment.setPaymentMode(request.getPaymentMode());
        payment.setReferenceNumber(request.getReferenceNumber());
        payment.setNotes(request.getNotes());

        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getFarmer().getId(),
                payment.getFarmer().getFarmerCode(),
                payment.getFarmer().getFullName(),
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getPaymentMode(),
                payment.getReferenceNumber(),
                payment.getNotes()
        );
    }
}