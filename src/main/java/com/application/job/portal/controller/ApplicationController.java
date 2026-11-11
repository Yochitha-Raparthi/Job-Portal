package com.application.job.portal.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.application.job.portal.dto.ApplicationResponse;
import com.application.job.portal.model.Application;
import com.application.job.portal.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Job Seeker applies for a job.
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/create")
    public ApplicationResponse applyForJob(
            @Valid @RequestBody Application a,
            Authentication authentication) {

        return applicationService.applyForJob(
                a, authentication.getName());
    }

    // Recruiter sees applications for their own jobs.
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/allApplications")
    public List<ApplicationResponse> getAllApplications(
            Authentication authentication) {

        return applicationService.getAllApplications(
                authentication.getName());
    }

    // Applicant or owning recruiter can view the application.
    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getApplicationById/{id}")
    public ApplicationResponse getApplicationById(
            @PathVariable Long id,
            Authentication authentication) {

        return applicationService.getApplicationById(
                id, authentication.getName());
    }

    // Job Seeker sees their own applications.
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/myApplications")
    public List<ApplicationResponse> getApplicationsByUser(
            Authentication authentication) {

        return applicationService.getApplicationsByUser(
                authentication.getName());
    }

    // Recruiter sees applications for a job they own.
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/getApplicationsByJob/{jobId}")
    public List<ApplicationResponse> getApplicationsByJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        return applicationService.getApplicationsByJob(
                jobId, authentication.getName());
    }

    // Recruiter updates status only for applications to their own jobs.
    @PreAuthorize("hasRole('RECRUITER')")
    @PatchMapping("/updateApplicationStatus/{id}")
    public ApplicationResponse updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {

        return applicationService.updateApplicationStatus(
                id, status, authentication.getName());
    }

    // Job Seeker withdraws only their own application.
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @DeleteMapping("/withdraw/{id}")
    public String withdrawApplication(
            @PathVariable Long id,
            Authentication authentication) {

        applicationService.withdrawApplication(
                id, authentication.getName());

        return "Application " + id + " withdrawn successfully";
    }
}