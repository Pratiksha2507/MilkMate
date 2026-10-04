package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.entity.MonthlyAccount;
import com.milkmate.entity.Payment;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.repository.MonthlyAccountRepository;
import com.milkmate.repository.PaymentRepository;
import com.milkmate.service.MonthlyAccountService;

@Service
public class MonthlyAccountServiceImpl
        implements MonthlyAccountService {

    private final MonthlyAccountRepository monthlyAccountRepository;
    private final FarmerRepository farmerRepository;
    private final MilkCollectionRepository milkCollectionRepository;
    private final PaymentRepository paymentRepository;

    public MonthlyAccountServiceImpl(
            MonthlyAccountRepository monthlyAccountRepository,
            FarmerRepository farmerRepository,
            MilkCollectionRepository milkCollectionRepository,
            PaymentRepository paymentRepository) {

        this.monthlyAccountRepository =
                monthlyAccountRepository;

        this.farmerRepository =
                farmerRepository;

        this.milkCollectionRepository =
                milkCollectionRepository;

        this.paymentRepository =
                paymentRepository;
    }


    @Override
    public MonthlyAccount generateMonthlyAccount(
            Long farmerId,
            Integer month,
            Integer year) {

        validateMonthAndYear(
                month,
                year
        );


        Farmer farmer =
                farmerRepository
                        .findById(farmerId)
                        .orElseThrow(
                                () ->
                                new ResourceNotFoundException(
                                        "Farmer not found with id: "
                                                + farmerId
                                )
                        );


        LocalDate startDate =
                LocalDate.of(
                        year,
                        month,
                        1
                );


        LocalDate endDate =
                startDate.withDayOfMonth(
                        startDate.lengthOfMonth()
                );


        List<MilkCollection> collections =
                milkCollectionRepository
                        .findByFarmerAndCollectionDateBetween(
                                farmer,
                                startDate,
                                endDate
                        );


        List<Payment> payments =
                paymentRepository
                        .findByFarmerIdAndPaymentDateBetween(
                                farmerId,
                                startDate,
                                endDate
                        );


        BigDecimal totalMilk =
                BigDecimal.ZERO;

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        BigDecimal paidAmount =
                BigDecimal.ZERO;


        for (MilkCollection collection :
                collections) {

            if (
                collection.getQuantity() != null
            ) {

                totalMilk =
                        totalMilk.add(
                                collection.getQuantity()
                        );
            }


            if (
                collection.getTotalAmount() != null
            ) {

                totalAmount =
                        totalAmount.add(
                                collection.getTotalAmount()
                        );
            }
        }


        for (Payment payment :
                payments) {

            if (
                payment.getAmount() != null
            ) {

                paidAmount =
                        paidAmount.add(
                                payment.getAmount()
                        );
            }
        }


        BigDecimal balanceAmount =
                totalAmount.subtract(
                        paidAmount
                );


        String status;


        if (
            paidAmount.compareTo(
                    BigDecimal.ZERO
            ) == 0
        ) {

            status = "PENDING";

        } else if (
            paidAmount.compareTo(
                    totalAmount
            ) < 0
        ) {

            status = "PARTIAL";

        } else if (
            paidAmount.compareTo(
                    totalAmount
            ) == 0
        ) {

            status = "PAID";

        } else {

            status = "OVERPAID";
        }


        MonthlyAccount account =
                monthlyAccountRepository
                        .findByFarmerIdAndAccountMonthAndAccountYear(
                                farmerId,
                                month,
                                year
                        )
                        .orElseGet(
                                MonthlyAccount::new
                        );


        account.setFarmer(
                farmer
        );

        account.setAccountMonth(
                month
        );

        account.setAccountYear(
                year
        );

        account.setTotalMilk(
                totalMilk
        );

        account.setTotalAmount(
                totalAmount
        );

        account.setPaidAmount(
                paidAmount
        );

        account.setBalanceAmount(
                balanceAmount
        );

        account.setStatus(
                status
        );


        return monthlyAccountRepository.save(
                account
        );
    }


    @Override
    public MonthlyAccount getMonthlyAccountById(
            Long id) {

        return monthlyAccountRepository
                .findById(id)
                .orElseThrow(
                        () ->
                        new ResourceNotFoundException(
                                "Monthly account not found with id: "
                                        + id
                        )
                );
    }


    @Override
    public MonthlyAccount getMonthlyAccount(
            Long farmerId,
            Integer month,
            Integer year) {

        validateMonthAndYear(
                month,
                year
        );


        return monthlyAccountRepository
                .findByFarmerIdAndAccountMonthAndAccountYear(
                        farmerId,
                        month,
                        year
                )
                .orElseThrow(
                        () ->
                        new ResourceNotFoundException(
                                "Monthly account not found for farmer: "
                                        + farmerId
                        )
                );
    }


    @Override
    public List<MonthlyAccount>
            getAllMonthlyAccounts() {

        return monthlyAccountRepository
                .findAll();
    }


    @Override
    public List<MonthlyAccount>
            getAccountsByFarmer(
                    Long farmerId) {

        if (
            !farmerRepository.existsById(
                    farmerId
            )
        ) {

            throw new ResourceNotFoundException(
                    "Farmer not found with id: "
                            + farmerId
            );
        }


        return monthlyAccountRepository
                .findByFarmerIdOrderByAccountYearDescAccountMonthDesc(
                        farmerId
                );
    }


    @Override
    public List<MonthlyAccount>
            getAccountsByMonth(
                    Integer month,
                    Integer year) {

        validateMonthAndYear(
                month,
                year
        );


        return monthlyAccountRepository
                .findByAccountYearAndAccountMonth(
                        year,
                        month
                );
    }


    private void validateMonthAndYear(
            Integer month,
            Integer year) {

        if (
            month == null ||
            month < 1 ||
            month > 12
        ) {

            throw new IllegalArgumentException(
                    "Month must be between 1 and 12"
            );
        }


        if (
            year == null ||
            year < 2000
        ) {

            throw new IllegalArgumentException(
                    "Invalid year"
            );
        }
    }
}