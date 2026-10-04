package com.milkmate.service;

import java.time.LocalDate;

import com.milkmate.dto.FarmerReportResponse;

public interface FarmerReportService {

    FarmerReportResponse getFarmerReport(
            Long farmerId,
            LocalDate startDate,
            LocalDate endDate
    );

}