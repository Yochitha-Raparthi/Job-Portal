package com.application.job.portal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.job.portal.dto.RecruiterDashboardResponse;
import com.application.job.portal.dto.SeekerDashboardResponse;
import com.application.job.portal.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<RecruiterDashboardResponse>
            getRecruiterDashboard(Authentication authentication) {

        return ResponseEntity.ok(
                dashboardService.getRecruiterDashboard(
                        authentication.getName())
        );
    }

    @GetMapping("/seeker")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<SeekerDashboardResponse>
            getSeekerDashboard(Authentication authentication) {

        return ResponseEntity.ok(
                dashboardService.getSeekerDashboard(
                        authentication.getName())
        );
    }
}