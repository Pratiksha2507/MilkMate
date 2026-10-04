package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.dto.DateWiseAnalyticsResponse;
import com.milkmate.entity.MilkCollection;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.service.DateWiseAnalyticsService;

@Service
public class DateWiseAnalyticsServiceImpl
        implements DateWiseAnalyticsService {

    private final MilkCollectionRepository
            milkCollectionRepository;

    public DateWiseAnalyticsServiceImpl(
            MilkCollectionRepository milkCollectionRepository) {

        this.milkCollectionRepository =
                milkCollectionRepository;
    }


    @Override
    public DateWiseAnalyticsResponse
            getDateWiseAnalytics(
                    LocalDate startDate,
                    LocalDate endDate) {


        // ==========================================
        // VALIDATION
        // ==========================================

        if (startDate == null) {

            throw new IllegalArgumentException(
                    "Start date is required"
            );
        }


        if (endDate == null) {

            throw new IllegalArgumentException(
                    "End date is required"
            );
        }


        if (startDate.isAfter(endDate)) {

            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }


        // ==========================================
        // GET COLLECTIONS
        // ==========================================

        List<MilkCollection> collections =
                milkCollectionRepository
                        .findByCollectionDateBetween(
                                startDate,
                                endDate
                        );


        // ==========================================
        // INITIAL VALUES
        // ==========================================

        BigDecimal totalMilk =
                BigDecimal.ZERO;

        BigDecimal morningMilk =
                BigDecimal.ZERO;

        BigDecimal eveningMilk =
                BigDecimal.ZERO;

        BigDecimal totalAmount =
                BigDecimal.ZERO;


        // ==========================================
        // CALCULATE
        // ==========================================

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


            totalMilk =
                    totalMilk.add(
                            quantity
                    );


            totalAmount =
                    totalAmount.add(
                            amount
                    );


            if ("MORNING".equalsIgnoreCase(
                    collection.getSession()
            )) {

                morningMilk =
                        morningMilk.add(
                                quantity
                        );
            }


            if ("EVENING".equalsIgnoreCase(
                    collection.getSession()
            )) {

                eveningMilk =
                        eveningMilk.add(
                                quantity
                        );
            }
        }


        // ==========================================
        // RESPONSE
        // ==========================================

        DateWiseAnalyticsResponse response =
                new DateWiseAnalyticsResponse();


        response.setStartDate(
                startDate.toString()
        );


        response.setEndDate(
                endDate.toString()
        );


        response.setTotalMilk(
                totalMilk
        );


        response.setMorningMilk(
                morningMilk
        );


        response.setEveningMilk(
                eveningMilk
        );


        response.setTotalAmount(
                totalAmount
        );


        response.setTotalCollections(
                collections.size()
        );


        return response;
    }
}