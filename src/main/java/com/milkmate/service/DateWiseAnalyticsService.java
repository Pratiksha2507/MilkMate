package com.milkmate.service;

import java.time.LocalDate;

import com.milkmate.dto.DateWiseAnalyticsResponse;

public interface DateWiseAnalyticsService {

    DateWiseAnalyticsResponse getDateWiseAnalytics(
            LocalDate startDate,
            LocalDate endDate);

}