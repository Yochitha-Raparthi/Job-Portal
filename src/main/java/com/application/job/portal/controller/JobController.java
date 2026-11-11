package com.application.job.portal.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.application.job.portal.dto.JobResponse;
import com.application.job.portal.model.Job;
import com.application.job.portal.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/create")
    public JobResponse createJob(
            @Valid @RequestBody Job j,
            Authentication authentication) {

        return jobService.createJob(j, authentication.getName());
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getAll")
    public List<JobResponse> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getJobById/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getJobByCompany/{companyId}")
    public List<JobResponse> getJobsByCompany(
            @PathVariable Long companyId) {

        return jobService.getJobsByCompany(companyId);
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getJobByTitle/{title}")
    public List<JobResponse> searchJobsByTitle(
            @PathVariable String title) {

        return jobService.searchJobsByTitle(title);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/updateJob/{id}")
    public JobResponse updateJob(
            @Valid @RequestBody Job j,
            @PathVariable Long id,
            Authentication authentication) {

        return jobService.updateJob(j, id, authentication.getName());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/deleteJob/{id}")
    public String deleteJob(
            @PathVariable Long id,
            Authentication authentication) {

        jobService.deleteJob(id, authentication.getName());

        return "Job " + id + " Deleted";
    }
}