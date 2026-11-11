package com.application.job.portal.service;

import java.util.List;

import com.application.job.portal.dto.JobResponse;
import com.application.job.portal.model.Job;

public interface JobService {

    List<JobResponse> getAllJobs();

    JobResponse getJobById(Long id);

    List<JobResponse> getJobsByCompany(Long companyId);

    List<JobResponse> searchJobsByTitle(String title);

    JobResponse createJob(Job j, String email);

    JobResponse updateJob(Job j, Long id, String email);

    void deleteJob(Long id, String email);
}