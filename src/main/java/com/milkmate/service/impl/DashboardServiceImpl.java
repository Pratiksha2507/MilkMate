package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.dto.DashboardResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.entity.Payment;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.repository.PaymentRepository;
import com.milkmate.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final FarmerRepository farmerRepository;
    private final MilkCollectionRepository milkCollectionRepository;
    private final PaymentRepository paymentRepository;

    public DashboardServiceImpl(
            FarmerRepository farmerRepository,
            MilkCollectionRepository milkCollectionRepository,
            PaymentRepository paymentRepository) {

        this.farmerRepository = farmerRepository;
        this.milkCollectionRepository = milkCollectionRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public DashboardResponse getDashboard() {

        LocalDate today = LocalDate.now();

        List<Farmer> farmers =
                farmerRepository.findAll();

        long totalFarmers =
                farmers.size();

        long activeFarmers =
                farmers.stream()
                        .filter(farmer ->
                                Boolean.TRUE.equals(
                                        farmer.getActive()
                                )
                        )
                        .count();


        List<MilkCollection> collections =
                milkCollectionRepository
                        .findByCollectionDate(today);


        BigDecimal todayMilk =
                BigDecimal.ZERO;

        BigDecimal todayCollectionAmount =
                BigDecimal.ZERO;

        BigDecimal morningMilk =
                BigDecimal.ZERO;

        BigDecimal eveningMilk =
                BigDecimal.ZERO;


        for (MilkCollection collection :
                collections) {

            BigDecimal quantity =
                    collection.getQuantity() != null
                            ? collection.getQuantity()
                            : BigDecimal.ZERO;

            BigDecimal amount =
                    collection.getTotalAmount() != null
                            ? collection.getTotalAmount()
                            : BigDecimal.ZERO;


            todayMilk =
                    todayMilk.add(quantity);

            todayCollectionAmount =
                    todayCollectionAmount.add(amount);


            if ("MORNING".equalsIgnoreCase(
                    collection.getSession()
            )) {

                morningMilk =
                        morningMilk.add(quantity);
            }


            if ("EVENING".equalsIgnoreCase(
                    collection.getSession()
            )) {

                eveningMilk =
                        eveningMilk.add(quantity);
            }
        }


        List<Payment> payments =
                paymentRepository
                        .findByPaymentDateBetween(
                                today,
                                today
                        );


        BigDecimal totalPaid =
                BigDecimal.ZERO;


        for (Payment payment : payments) {

            if (payment.getAmount() != null) {

                totalPaid =
                        totalPaid.add(
                                payment.getAmount()
                        );
            }
        }


        BigDecimal pendingAmount =
                todayCollectionAmount.subtract(
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


        DashboardResponse response =
                new DashboardResponse();


        response.setTotalFarmers(
                totalFarmers
        );

        response.setActiveFarmers(
                activeFarmers
        );

        response.setTodayMilk(
                todayMilk
        );

        response.setTodayCollectionAmount(
                todayCollectionAmount
        );

        response.setMorningMilk(
                morningMilk
        );

        response.setEveningMilk(
                eveningMilk
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