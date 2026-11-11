package com.application.job.portal.service;

import java.util.List;

import com.application.job.portal.dto.ApplicationResponse;
import com.application.job.portal.model.Application;

public interface ApplicationService {

    ApplicationResponse applyForJob(Application a, String email);

    List<ApplicationResponse> getAllApplications(String email);

    ApplicationResponse getApplicationById(Long id, String email);

    List<ApplicationResponse> getApplicationsByUser(String email);

    List<ApplicationResponse> getApplicationsByJob(
            Long jobId, String email);

    ApplicationResponse updateApplicationStatus(
            Long id, String status, String email);

    void withdrawApplication(Long id, String email);
}