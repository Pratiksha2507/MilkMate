package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.dto.FarmerReportResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.service.FarmerReportService;

@Service
public class FarmerReportServiceImpl implements FarmerReportService {

    private final FarmerRepository farmerRepository;

    private final MilkCollectionRepository milkCollectionRepository;

    public FarmerReportServiceImpl(
            FarmerRepository farmerRepository,
            MilkCollectionRepository milkCollectionRepository) {

        this.farmerRepository = farmerRepository;
        this.milkCollectionRepository =
                milkCollectionRepository;
    }

    @Override
    public FarmerReportResponse getFarmerReport(
            Long farmerId,
            LocalDate startDate,
            LocalDate endDate) {

        Farmer farmer =
                farmerRepository.findById(farmerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Farmer not found"
                                )
                        );

        List<MilkCollection> collections =
                milkCollectionRepository
                        .findByFarmerIdAndCollectionDateBetween(
                                farmerId,
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

        long totalCollections =
                collections.size();

        for (MilkCollection collection : collections) {

            BigDecimal quantity =
                    collection.getQuantity() != null
                            ? collection.getQuantity()
                            : BigDecimal.ZERO;

            BigDecimal amount =
                    collection.getTotalAmount() != null
                            ? collection.getTotalAmount()
                            : BigDecimal.ZERO;

            totalMilk =
                    totalMilk.add(quantity);

            totalAmount =
                    totalAmount.add(amount);

            if (collection.getSession() != null) {

                String session =
                        collection.getSession()
                                .toString()
                                .toUpperCase();

                if (session.equals("MORNING")) {

                    morningMilk =
                            morningMilk.add(quantity);

                } else if (session.equals("EVENING")) {

                    eveningMilk =
                            eveningMilk.add(quantity);
                }
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
                totalCollections
        );

        return response;
    }
}