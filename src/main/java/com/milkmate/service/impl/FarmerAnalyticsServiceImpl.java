package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.dto.FarmerAnalyticsResponse;
import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.service.FarmerAnalyticsService;

@Service
public class FarmerAnalyticsServiceImpl
        implements FarmerAnalyticsService {

    private final FarmerRepository
            farmerRepository;

    private final MilkCollectionRepository
            milkCollectionRepository;


    public FarmerAnalyticsServiceImpl(
            FarmerRepository farmerRepository,
            MilkCollectionRepository milkCollectionRepository) {

        this.farmerRepository =
                farmerRepository;

        this.milkCollectionRepository =
                milkCollectionRepository;
    }


    @Override
    public List<FarmerAnalyticsResponse>
            getFarmerAnalytics() {

        List<Farmer> farmers =
                farmerRepository.findAll();


        List<FarmerAnalyticsResponse>
                responseList =
                new ArrayList<>();


        for (
            Farmer farmer :
            farmers
        ) {


            List<MilkCollection>
                    collections =
                    milkCollectionRepository
                            .findByFarmer(
                                    farmer
                            );


            BigDecimal totalMilk =
                    BigDecimal.ZERO;

            BigDecimal morningMilk =
                    BigDecimal.ZERO;

            BigDecimal eveningMilk =
                    BigDecimal.ZERO;

            BigDecimal totalAmount =
                    BigDecimal.ZERO;


            for (
                MilkCollection collection :
                collections
            ) {


                // ==================================
                // TOTAL MILK
                // ==================================

                if (
                    collection.getQuantity()
                            != null
                ) {

                    totalMilk =
                            totalMilk.add(
                                    collection.getQuantity()
                            );
                }


                // ==================================
                // TOTAL AMOUNT
                // ==================================

                if (
                    collection.getTotalAmount()
                            != null
                ) {

                    totalAmount =
                            totalAmount.add(
                                    collection.getTotalAmount()
                            );
                }


                // ==================================
                // MORNING
                // ==================================

                if (
                    "MORNING"
                        .equalsIgnoreCase(
                            collection.getSession()
                        )
                ) {

                    if (
                        collection.getQuantity()
                                != null
                    ) {

                        morningMilk =
                                morningMilk.add(
                                    collection
                                        .getQuantity()
                                );
                    }
                }


                // ==================================
                // EVENING
                // ==================================

                if (
                    "EVENING"
                        .equalsIgnoreCase(
                            collection.getSession()
                        )
                ) {

                    if (
                        collection.getQuantity()
                                != null
                    ) {

                        eveningMilk =
                                eveningMilk.add(
                                    collection
                                        .getQuantity()
                                );
                    }
                }
            }


            // ======================================
            // RESPONSE
            // ======================================

            FarmerAnalyticsResponse response =
                    new FarmerAnalyticsResponse();


            response.setFarmerId(
                    farmer.getId()
            );


            response.setFarmerCode(
                    farmer.getFarmerCode()
            );


            response.setFarmerName(
                    farmer.getFullName()
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


            responseList.add(
                    response
            );
        }


        return responseList;
    }
}