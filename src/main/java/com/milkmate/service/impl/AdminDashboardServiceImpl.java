package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.dto.AdminDashboardResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.entity.Payment;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.repository.PaymentRepository;
import com.milkmate.service.AdminDashboardService;

@Service
public class AdminDashboardServiceImpl
        implements AdminDashboardService {

    private final FarmerRepository farmerRepository;
    private final MilkCollectionRepository milkCollectionRepository;
    private final PaymentRepository paymentRepository;

    public AdminDashboardServiceImpl(
            FarmerRepository farmerRepository,
            MilkCollectionRepository milkCollectionRepository,
            PaymentRepository paymentRepository) {

        this.farmerRepository = farmerRepository;
        this.milkCollectionRepository = milkCollectionRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public AdminDashboardResponse getDashboardData() {

        LocalDate today = LocalDate.now();

        AdminDashboardResponse response =
                new AdminDashboardResponse();

        // ==========================================
        // FARMERS
        // ==========================================

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

        response.setTotalFarmers(
                totalFarmers
        );

        response.setActiveFarmers(
                activeFarmers
        );


        // ==========================================
        // TODAY'S COLLECTION
        // ==========================================

        List<MilkCollection> todayCollections =
                milkCollectionRepository
                        .findByCollectionDate(today);

        BigDecimal todayMilk =
                BigDecimal.ZERO;

        BigDecimal todayCollection =
                BigDecimal.ZERO;

        BigDecimal morningMilk =
                BigDecimal.ZERO;

        BigDecimal eveningMilk =
                BigDecimal.ZERO;


        for (MilkCollection collection :
                todayCollections) {

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

            todayCollection =
                    todayCollection.add(amount);


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


        response.setTodayMilk(
                todayMilk
        );

        response.setTodayCollection(
                todayCollection
        );

        response.setMorningMilk(
                morningMilk
        );

        response.setEveningMilk(
                eveningMilk
        );


        // ==========================================
        // TOTAL PAID
        // ==========================================

        List<Payment> payments =
                paymentRepository.findAll();

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


        response.setTotalPaid(
                totalPaid
        );


        // ==========================================
        // OVERALL PENDING AMOUNT
        // ==========================================

        List<MilkCollection> allCollections =
                milkCollectionRepository.findAll();

        BigDecimal totalCollectionAmount =
                BigDecimal.ZERO;


        for (MilkCollection collection :
                allCollections) {

            if (collection.getTotalAmount() != null) {

                totalCollectionAmount =
                        totalCollectionAmount.add(
                                collection.getTotalAmount()
                        );
            }
        }


        BigDecimal pendingAmount =
                totalCollectionAmount.subtract(
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


        response.setPendingAmount(
                pendingAmount
        );


        return response;
    }
}