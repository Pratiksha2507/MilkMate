package com.milkmate.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.milkmate.entity.Farmer;
import com.milkmate.entity.MilkCollection;
import com.milkmate.entity.MilkRate;
import com.milkmate.entity.NotificationType;
import com.milkmate.exception.ResourceNotFoundException;
import com.milkmate.repository.FarmerRepository;
import com.milkmate.repository.MilkCollectionRepository;
import com.milkmate.service.MilkCollectionService;
import com.milkmate.service.MilkRateService;
import com.milkmate.service.NotificationService;

@Service
public class MilkCollectionServiceImpl
        implements MilkCollectionService {

    private final MilkCollectionRepository
            milkCollectionRepository;

    private final FarmerRepository
            farmerRepository;

    private final NotificationService
            notificationService;

    private final MilkRateService
            milkRateService;


    public MilkCollectionServiceImpl(
            MilkCollectionRepository milkCollectionRepository,
            FarmerRepository farmerRepository,
            NotificationService notificationService,
            MilkRateService milkRateService) {

        this.milkCollectionRepository =
                milkCollectionRepository;

        this.farmerRepository =
                farmerRepository;

        this.notificationService =
                notificationService;

        this.milkRateService =
                milkRateService;
    }


    // ==========================================
    // CREATE
    // ==========================================

    @Override
    public MilkCollection createMilkCollection(
            Long farmerId,
            MilkCollection milkCollection) {

        Farmer farmer =
                farmerRepository
                        .findById(farmerId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Farmer not found with id: "
                                                + farmerId
                                )
                        );


        validateMilkCollection(
                milkCollection
        );


        String session =
                normalizeSession(
                        milkCollection.getSession()
                );

        milkCollection.setSession(
                session
        );

        milkCollection.setFarmer(
                farmer
        );


        boolean duplicate =
                milkCollectionRepository
                        .existsByFarmerAndCollectionDateAndSession(
                                farmer,
                                milkCollection
                                        .getCollectionDate(),
                                session
                        );


        if (duplicate) {

            throw new IllegalArgumentException(
                    "Milk collection already exists for this farmer, date and session"
            );
        }


        // Automatic rate
        MilkRate applicableRate =
                milkRateService.getApplicableRate(
                        milkCollection
                                .getCollectionDate()
                );


        milkCollection.setRate(
                applicableRate.getRatePerLiter()
        );


        calculateTotalAmount(
                milkCollection
        );


        MilkCollection savedCollection =
                milkCollectionRepository.save(
                        milkCollection
                );


        String message =
                savedCollection.getQuantity()
                + " L milk collected for "
                + savedCollection.getSession()
                + " session. Rate: ₹"
                + savedCollection.getRate()
                + "/L. Total amount: ₹"
                + savedCollection.getTotalAmount();


        notificationService.createNotification(
                "Milk Collection Recorded",
                message,
                NotificationType.MILK_COLLECTION,
                farmer.getId()
        );


        return savedCollection;
    }


    // ==========================================
    // GET BY ID
    // ==========================================

    @Override
    public MilkCollection getMilkCollectionById(
            Long id) {

        return milkCollectionRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Milk collection not found with id: "
                                        + id
                        )
                );
    }


    // ==========================================
    // GET ALL
    // ==========================================

    @Override
    public List<MilkCollection>
            getAllMilkCollections() {

        return milkCollectionRepository
                .findAll();
    }


    // ==========================================
    // GET BY FARMER
    // ==========================================

    @Override
    public List<MilkCollection>
            getCollectionsByFarmer(
                    Long farmerId) {

        Farmer farmer =
                farmerRepository
                        .findById(farmerId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Farmer not found with id: "
                                                + farmerId
                                )
                        );

        return milkCollectionRepository
                .findByFarmer(farmer);
    }


    // ==========================================
    // DATE RANGE
    // ==========================================

    @Override
    public List<MilkCollection>
            getCollectionsByFarmerAndDateRange(
                    Long farmerId,
                    LocalDate startDate,
                    LocalDate endDate) {

        Farmer farmer =
                farmerRepository
                        .findById(farmerId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Farmer not found with id: "
                                                + farmerId
                                )
                        );


        if (
            startDate == null ||
            endDate == null
        ) {

            throw new IllegalArgumentException(
                    "Start date and end date are required"
            );
        }


        if (startDate.isAfter(endDate)) {

            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }


        return milkCollectionRepository
                .findByFarmerAndCollectionDateBetween(
                        farmer,
                        startDate,
                        endDate
                );
    }


    // ==========================================
    // UPDATE
    // ==========================================

    @Override
    public MilkCollection updateMilkCollection(
            Long id,
            MilkCollection milkCollection) {

        MilkCollection existing =
                milkCollectionRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Milk collection not found with id: "
                                                + id
                                )
                        );


        validateMilkCollection(
                milkCollection
        );


        String session =
                normalizeSession(
                        milkCollection.getSession()
                );


        Farmer farmer =
                existing.getFarmer();


        MilkCollection duplicate =
                milkCollectionRepository
                        .findByFarmerAndCollectionDateAndSession(
                                farmer,
                                milkCollection
                                        .getCollectionDate(),
                                session
                        )
                        .orElse(null);


        if (
            duplicate != null &&
            !duplicate.getId()
                    .equals(existing.getId())
        ) {

            throw new IllegalArgumentException(
                    "Milk collection already exists for this farmer, date and session"
            );
        }


        MilkRate applicableRate =
                milkRateService.getApplicableRate(
                        milkCollection
                                .getCollectionDate()
                );


        existing.setCollectionDate(
                milkCollection
                        .getCollectionDate()
        );

        existing.setSession(
                session
        );

        existing.setQuantity(
                milkCollection.getQuantity()
        );

        existing.setFat(
                milkCollection.getFat()
        );

        existing.setSnf(
                milkCollection.getSnf()
        );

        existing.setRate(
                applicableRate.getRatePerLiter()
        );


        calculateTotalAmount(
                existing
        );


        return milkCollectionRepository.save(
                existing
        );
    }


    // ==========================================
    // DELETE
    // ==========================================

    @Override
    public void deleteMilkCollection(
            Long id) {

        if (
            !milkCollectionRepository
                    .existsById(id)
        ) {

            throw new ResourceNotFoundException(
                    "Milk collection not found with id: "
                            + id
            );
        }


        milkCollectionRepository
                .deleteById(id);
    }


    // ==========================================
    // VALIDATION
    // ==========================================

    private void validateMilkCollection(
            MilkCollection milkCollection) {

        if (milkCollection == null) {

            throw new IllegalArgumentException(
                    "Milk collection data is required"
            );
        }


        if (
            milkCollection
                    .getCollectionDate()
                    == null
        ) {

            throw new IllegalArgumentException(
                    "Collection date is required"
            );
        }


        if (
            milkCollection.getSession()
                    == null ||
            milkCollection.getSession()
                    .isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Session is required"
            );
        }


        if (
            milkCollection.getQuantity()
                    == null ||
            milkCollection.getQuantity()
                    .compareTo(
                            BigDecimal.ZERO
                    ) <= 0
        ) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }


        if (
            milkCollection.getFat()
                    != null &&
            milkCollection.getFat()
                    .compareTo(
                            BigDecimal.ZERO
                    ) < 0
        ) {

            throw new IllegalArgumentException(
                    "Fat cannot be negative"
            );
        }


        if (
            milkCollection.getSnf()
                    != null &&
            milkCollection.getSnf()
                    .compareTo(
                            BigDecimal.ZERO
                    ) < 0
        ) {

            throw new IllegalArgumentException(
                    "SNF cannot be negative"
            );
        }
    }


    // ==========================================
    // SESSION
    // ==========================================

    private String normalizeSession(
            String session) {

        String normalized =
                session.trim()
                        .toUpperCase();


        if (
            !normalized.equals("MORNING") &&
            !normalized.equals("EVENING")
        ) {

            throw new IllegalArgumentException(
                    "Session must be MORNING or EVENING"
            );
        }


        return normalized;
    }


    // ==========================================
    // TOTAL AMOUNT
    // ==========================================

    private void calculateTotalAmount(
            MilkCollection milkCollection) {

        BigDecimal total =
                milkCollection
                        .getQuantity()
                        .multiply(
                                milkCollection
                                        .getRate()
                        );


        milkCollection.setTotalAmount(
                total
        );
    }
}