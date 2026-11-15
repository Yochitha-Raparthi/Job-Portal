package com.application.job.portal.service;

import com.application.job.portal.dto.RecruiterDashboardResponse;
import com.application.job.portal.dto.SeekerDashboardResponse;

public interface DashboardService {

    RecruiterDashboardResponse getRecruiterDashboard(String email);

    SeekerDashboardResponse getSeekerDashboard(String email);
}
