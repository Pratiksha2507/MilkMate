package com.milkmate.service;

import java.util.List;

import com.milkmate.entity.MonthlyAccount;

public interface MonthlyAccountService {

    MonthlyAccount generateMonthlyAccount(
            Long farmerId,
            Integer month,
            Integer year
    );

    MonthlyAccount getMonthlyAccountById(Long id);

    MonthlyAccount getMonthlyAccount(
            Long farmerId,
            Integer month,
            Integer year
    );

    List<MonthlyAccount> getAllMonthlyAccounts();

    List<MonthlyAccount> getAccountsByFarmer(Long farmerId);

    List<MonthlyAccount> getAccountsByMonth(
            Integer month,
            Integer year
    );
}