package com.application.job.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.job.portal.dto.ApplicationResponse;
import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Application;
import com.application.job.portal.model.Job;
import com.application.job.portal.model.User;
import com.application.job.portal.repo.ApplicationRepository;
import com.application.job.portal.repo.JobRepository;
import com.application.job.portal.repo.UserRepository;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepo;
    private final UserRepository userRepo;
    private final JobRepository jobRepo;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepo,
            UserRepository userRepo,
            JobRepository jobRepo) {

        this.applicationRepo = applicationRepo;
        this.userRepo = userRepo;
        this.jobRepo = jobRepo;
    }

    // Convert Application entity into ApplicationResponse DTO.
    private ApplicationResponse convertToResponse(Application application) {

        return new ApplicationResponse(
                application.getId(),
                application.getApplicant().getId(),
                application.getApplicant().getName(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getStatus(),
                application.getResumeUrl(),
                application.getAppliedAt()
        );
    }

    // Job Seeker applies for a job.
    @Override
    public ApplicationResponse applyForJob(Application a, String email) {

        User applicant = userRepo.findByEmail(email)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));

        if (a.getJob() == null || a.getJob().getId() == null) {
            throw new ResourceNotFoundException("Job is required");
        }

        Job job = jobRepo.findById(a.getJob().getId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Job not found"));

        // Assign the authenticated user, not the ID from the request.
        a.setApplicant(applicant);
        a.setJob(job);
        a.setAppliedAt(LocalDateTime.now());

        Application savedApplication = applicationRepo.save(a);

        return convertToResponse(savedApplication);
    }

    // Recruiter can view applications only for their own jobs.
    @Override
    public List<ApplicationResponse> getAllApplications(String email) {

        return applicationRepo
                .findByJob_Company_Recruiter_Email(email)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Applicant or owning recruiter can view an application.
    @Override
    public ApplicationResponse getApplicationById(Long id, String email) {

        Application application = applicationRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Application not found with ID: " + id));

        boolean isApplicant =
                application.getApplicant().getEmail().equals(email);

        boolean isOwningRecruiter =
                application.getJob().getCompany()
                    .getRecruiter().getEmail().equals(email);

        if (!isApplicant && !isOwningRecruiter) {
            throw new AccessDeniedException(
                "You cannot view another user's application");
        }

        return convertToResponse(application);
    }

    // Job Seeker can view only their own applications.
    @Override
    public List<ApplicationResponse> getApplicationsByUser(String email) {

        return applicationRepo.findByApplicant_Email(email)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Recruiter can view applications only for their own job.
    @Override
    public List<ApplicationResponse> getApplicationsByJob(
            Long jobId, String email) {

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Job not found"));

        if (!job.getCompany().getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot view applications for another recruiter's job");
        }

        return applicationRepo
                .findByJob_IdAndJob_Company_Recruiter_Email(jobId, email)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Only the recruiter who owns the job can update application status.
    @Override
    public ApplicationResponse updateApplicationStatus(
            Long id, String status, String email) {

        Application application = applicationRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Application not found with ID: " + id));

        String recruiterEmail = application.getJob()
                .getCompany().getRecruiter().getEmail();

        if (!recruiterEmail.equals(email)) {
            throw new AccessDeniedException(
                "You cannot update another recruiter's application");
        }

        application.setStatus(status);

        Application updatedApplication =
                applicationRepo.save(application);

        return convertToResponse(updatedApplication);
    }

    // Only the applicant can withdraw their application.
    @Override
    public void withdrawApplication(Long id, String email) {

        Application application = applicationRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Application not found with ID: " + id));

        if (!application.getApplicant().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot withdraw another user's application");
        }

        applicationRepo.delete(application);
    }
}