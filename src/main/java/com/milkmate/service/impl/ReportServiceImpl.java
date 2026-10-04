package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.dto.FarmerReportResponse;
import com.milkmate.dto.ReportResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.service.ReportService;

@Service
public class ReportServiceImpl
        implements ReportService {

    private final MilkCollectionRepository
            milkCollectionRepository;

    private final FarmerRepository
            farmerRepository;


    public ReportServiceImpl(
            MilkCollectionRepository milkCollectionRepository,
            FarmerRepository farmerRepository) {

        this.milkCollectionRepository =
                milkCollectionRepository;

        this.farmerRepository =
                farmerRepository;
    }


    // ==========================================
    // DATE-WISE REPORT
    // ==========================================

    @Override
    public ReportResponse getDateWiseReport(
            LocalDate date) {

        if (date == null) {

            throw new IllegalArgumentException(
                    "Report date is required"
            );
        }


        List<MilkCollection> collections =
                milkCollectionRepository
                        .findByCollectionDate(
                                date
                        );


        BigDecimal totalMilk =
                BigDecimal.ZERO;

        BigDecimal totalAmount =
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


        ReportResponse response =
                new ReportResponse();


        response.setReportDate(
                date.toString()
        );


        response.setTotalMilk(
                totalMilk
        );


        response.setTotalAmount(
                totalAmount
        );


        response.setMorningMilk(
                morningMilk
        );


        response.setEveningMilk(
                eveningMilk
        );


        response.setTotalCollections(
                collections.size()
        );


        return response;
    }


    // ==========================================
    // FARMER-WISE REPORT
    // ==========================================

    @Override
    public FarmerReportResponse
            getFarmerWiseReport(
                    Long farmerId,
                    LocalDate startDate,
                    LocalDate endDate) {


        if (farmerId == null) {

            throw new IllegalArgumentException(
                    "Farmer ID is required"
            );
        }


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


        List<MilkCollection> collections =
                milkCollectionRepository
                        .findByFarmerAndCollectionDateBetween(
                                farmer,
                                startDate,
                                endDate
                        );


        BigDecimal totalMilk =
                BigDecimal.ZERO;

        BigDecimal totalAmount =
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


        FarmerReportResponse response =
                new FarmerReportResponse();


        response.setFarmerId(
                farmer.getId()
        );


        response.setFarmerCode(
                farmer.getFarmerCode()
        );


        response.setFarmerName(
                farmer.getFullName()
        );


        response.setStartDate(
                startDate.toString()
        );


        response.setEndDate(
                endDate.toString()
        );


        response.setTotalMilk(
                totalMilk
        );


        response.setTotalAmount(
                totalAmount
        );


        response.setMorningMilk(
                morningMilk
        );


        response.setEveningMilk(
                eveningMilk
        );


        response.setTotalCollections(
                collections.size()
        );


        return response;
    }
}