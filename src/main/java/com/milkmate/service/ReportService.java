package com.milkmate.service;

import java.time.LocalDate;

import com.milkmate.dto.FarmerReportResponse;
import com.milkmate.dto.ReportResponse;

public interface ReportService {

    ReportResponse getDateWiseReport(LocalDate date);

    FarmerReportResponse getFarmerWiseReport(
            Long farmerId,
            LocalDate startDate,
            LocalDate endDate);
}