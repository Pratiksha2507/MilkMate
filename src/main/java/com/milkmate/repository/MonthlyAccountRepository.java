package com.milkmate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milkmate.entity.MonthlyAccount;

public interface MonthlyAccountRepository
        extends JpaRepository<MonthlyAccount, Long> {

    Optional<MonthlyAccount> findByFarmerIdAndAccountMonthAndAccountYear(
            Long farmerId,
            Integer accountMonth,
            Integer accountYear
    );

    List<MonthlyAccount> findByFarmerIdOrderByAccountYearDescAccountMonthDesc(
            Long farmerId
    );

    List<MonthlyAccount> findByAccountYearAndAccountMonth(
            Integer accountYear,
            Integer accountMonth
    );
}