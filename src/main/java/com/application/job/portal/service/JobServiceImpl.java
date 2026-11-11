package com.application.job.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.application.job.portal.dto.JobResponse;
import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Company;
import com.application.job.portal.model.Job;
import com.application.job.portal.repo.CompanyRepository;
import com.application.job.portal.repo.JobRepository;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepo;
    private final CompanyRepository companyRepo;

    public JobServiceImpl(
            JobRepository jobRepo,
            CompanyRepository companyRepo) {
        this.jobRepo = jobRepo;
        this.companyRepo = companyRepo;
    }

    // Convert Job entity into JobResponse DTO
    private JobResponse convertToResponse(Job job) {

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getSalary(),
                job.getJobType(),
                job.getCompany().getId(),
                job.getCompany().getCompanyName(),
                job.getPostedAt()
        );
    }

    @Override
    public JobResponse createJob(Job j, String email) {

        if (j.getCompany() == null ||
                j.getCompany().getId() == null) {
            throw new ResourceNotFoundException("Company is required");
        }

        Company company = companyRepo.findById(j.getCompany().getId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Company not found"));

        // Check whether the logged-in recruiter owns this company.
        if (!company.getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot create a job for another recruiter's company");
        }

        j.setCompany(company);
        j.setPostedAt(LocalDateTime.now());

        Job savedJob = jobRepo.save(j);

        return convertToResponse(savedJob);
    }

    @Override
    public List<JobResponse> getAllJobs() {

        return jobRepo.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public JobResponse getJobById(Long id) {

        Job job = jobRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Job not found with Id: " + id));

        return convertToResponse(job);
    }

    @Override
    public List<JobResponse> getJobsByCompany(Long companyId) {

        return jobRepo.findByCompanyId(companyId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<JobResponse> searchJobsByTitle(String title) {

        return jobRepo.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public JobResponse updateJob(Job j, Long id, String email) {

        Job jobDB = jobRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Job not found with Id: " + id));

        // First, check ownership of the existing job.
        if (!jobDB.getCompany().getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot update another recruiter's job");
        }

        if (j.getCompany() == null ||
                j.getCompany().getId() == null) {
            throw new ResourceNotFoundException("Company is required");
        }

        Company newCompany = companyRepo.findById(j.getCompany().getId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Company not found"));

        // The recruiter must also own the company receiving the job.
        if (!newCompany.getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot assign a job to another recruiter's company");
        }

        jobDB.setTitle(j.getTitle());
        jobDB.setDescription(j.getDescription());
        jobDB.setJobType(j.getJobType());
        jobDB.setLocation(j.getLocation());
        jobDB.setSalary(j.getSalary());
        jobDB.setCompany(newCompany);

        Job updatedJob = jobRepo.save(jobDB);

        return convertToResponse(updatedJob);
    }

    @Override
    public void deleteJob(Long id, String email) {

        Job jobDB = jobRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Job not found with Id: " + id));

        if (!jobDB.getCompany().getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot delete another recruiter's job");
        }

        jobRepo.delete(jobDB);
    }
}