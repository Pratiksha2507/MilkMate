package com.milkmate.service;

import com.milkmate.dto.FarmerDashboardResponse;

public interface FarmerDashboardService {

    FarmerDashboardResponse getDashboard(Long farmerId);

}