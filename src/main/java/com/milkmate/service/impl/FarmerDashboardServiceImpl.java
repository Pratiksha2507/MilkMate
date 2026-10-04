package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.dto.FarmerDashboardResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.entity.Payment;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.repository.PaymentRepository;
import com.milkmate.service.FarmerDashboardService;

@Service
public class FarmerDashboardServiceImpl
        implements FarmerDashboardService {

    private final FarmerRepository farmerRepository;

    private final MilkCollectionRepository
            milkCollectionRepository;

    private final PaymentRepository
            paymentRepository;


    public FarmerDashboardServiceImpl(
            FarmerRepository farmerRepository,
            MilkCollectionRepository milkCollectionRepository,
            PaymentRepository paymentRepository) {

        this.farmerRepository =
                farmerRepository;

        this.milkCollectionRepository =
                milkCollectionRepository;

        this.paymentRepository =
                paymentRepository;
    }


    @Override
    public FarmerDashboardResponse
            getDashboard(Long farmerId) {

        // ==========================================
        // FARMER
        // ==========================================

        Farmer farmer =
                farmerRepository
                        .findById(
                                farmerId
                        )
                        .orElseThrow(
                                () ->
                                new ResourceNotFoundException(
                                        "Farmer not found with id: "
                                                + farmerId
                                )
                        );


        // ==========================================
        // DATES
        // ==========================================

        LocalDate today =
                LocalDate.now();

        LocalDate monthStart =
                today.withDayOfMonth(1);

        LocalDate monthEnd =
                today.withDayOfMonth(
                        today.lengthOfMonth()
                );


        // ==========================================
        // TODAY MILK
        // ==========================================

        List<MilkCollection>
                todayCollections =
                milkCollectionRepository
                        .findByFarmerIdAndCollectionDateBetween(
                                farmerId,
                                today,
                                today
                        );


        // ==========================================
        // MONTHLY MILK
        // ==========================================

        List<MilkCollection>
                monthlyCollections =
                milkCollectionRepository
                        .findByFarmerIdAndCollectionDateBetween(
                                farmerId,
                                monthStart,
                                monthEnd
                        );


        // ==========================================
        // MONTHLY PAYMENTS
        // ==========================================

        List<Payment>
                monthlyPayments =
                paymentRepository
                        .findByFarmerIdAndPaymentDateBetween(
                                farmerId,
                                monthStart,
                                monthEnd
                        );


        // ==========================================
        // TODAY CALCULATION
        // ==========================================

        BigDecimal todayMilk =
                BigDecimal.ZERO;

        BigDecimal todayAmount =
                BigDecimal.ZERO;


        for (
            MilkCollection collection :
            todayCollections
        ) {

            if (
                collection.getQuantity()
                        != null
            ) {

                todayMilk =
                        todayMilk.add(
                                collection.getQuantity()
                        );
            }


            if (
                collection.getTotalAmount()
                        != null
            ) {

                todayAmount =
                        todayAmount.add(
                                collection.getTotalAmount()
                        );
            }
        }


        // ==========================================
        // MONTH CALCULATION
        // ==========================================

        BigDecimal monthlyMilk =
                BigDecimal.ZERO;

        BigDecimal monthlyAmount =
                BigDecimal.ZERO;


        for (
            MilkCollection collection :
            monthlyCollections
        ) {

            if (
                collection.getQuantity()
                        != null
            ) {

                monthlyMilk =
                        monthlyMilk.add(
                                collection.getQuantity()
                        );
            }


            if (
                collection.getTotalAmount()
                        != null
            ) {

                monthlyAmount =
                        monthlyAmount.add(
                                collection.getTotalAmount()
                        );
            }
        }


        // ==========================================
        // PAYMENT CALCULATION
        // ==========================================

        BigDecimal totalPaid =
                BigDecimal.ZERO;


        for (
            Payment payment :
            monthlyPayments
        ) {

            if (
                payment.getAmount()
                        != null
            ) {

                totalPaid =
                        totalPaid.add(
                                payment.getAmount()
                        );
            }
        }


        // ==========================================
        // PENDING
        // ==========================================

        BigDecimal pendingAmount =
                monthlyAmount.subtract(
                        totalPaid
                );


        if (
            pendingAmount.compareTo(
                    BigDecimal.ZERO
            ) < 0
        ) {

            pendingAmount =
                    BigDecimal.ZERO;
        }


        // ==========================================
        // RESPONSE
        // ==========================================

        FarmerDashboardResponse response =
                new FarmerDashboardResponse();


        response.setFarmerId(
                farmer.getId()
        );

        response.setFarmerCode(
                farmer.getFarmerCode()
        );

        response.setFarmerName(
                farmer.getFullName()
        );

        response.setTodayMilk(
                todayMilk
        );

        response.setTodayAmount(
                todayAmount
        );

        response.setMonthlyMilk(
                monthlyMilk
        );

        response.setMonthlyAmount(
                monthlyAmount
        );

        response.setTotalPaid(
                totalPaid
        );

        response.setPendingAmount(
                pendingAmount
        );


        return response;
    }
}