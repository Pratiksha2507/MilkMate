package com.milkmate.util;

import org.springframework.stereotype.Component;

import com.milkmate.dto.MonthlyAccountResponse;
import com.milkmate.entity.MonthlyAccount;

@Component
public class MonthlyAccountMapper {

    public MonthlyAccountResponse toResponse(
            MonthlyAccount account) {

        return new MonthlyAccountResponse(
                account.getId(),
                account.getFarmer().getId(),
                account.getFarmer().getFarmerCode(),
                account.getFarmer().getFullName(),
                account.getAccountMonth(),
                account.getAccountYear(),
                account.getTotalMilk(),
                account.getTotalAmount(),
                account.getPaidAmount(),
                account.getBalanceAmount(),
                account.getStatus()
        );
    }
}