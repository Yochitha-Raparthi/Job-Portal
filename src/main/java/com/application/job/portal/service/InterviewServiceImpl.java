package com.application.job.portal.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.job.portal.dto.InterviewResponse;
import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Application;
import com.application.job.portal.model.Interview;
import com.application.job.portal.repo.ApplicationRepository;
import com.application.job.portal.repo.InterviewRepository;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepo;
    private final ApplicationRepository applicationRepo;

    public InterviewServiceImpl(
            InterviewRepository interviewRepo,
            ApplicationRepository applicationRepo) {

        this.interviewRepo = interviewRepo;
        this.applicationRepo = applicationRepo;
    }

    // Convert Interview entity into InterviewResponse DTO.
    private InterviewResponse convertToResponse(Interview interview) {

        Application application = interview.getApplication();

        return new InterviewResponse(
                interview.getId(),
                application.getId(),
                application.getApplicant().getId(),
                application.getApplicant().getName(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                interview.getInterviewDate(),
                interview.getInterviewMode(),
                interview.getMeetingLink(),
                interview.getStatus()
        );
    }

    // Check whether the logged-in recruiter owns the job.
    private void checkRecruiterOwnership(
            Application application, String email) {

        String recruiterEmail = application.getJob()
                .getCompany()
                .getRecruiter()
                .getEmail();

        if (!recruiterEmail.equals(email)) {
            throw new AccessDeniedException(
                    "You are not authorized to access this interview");
        }
    }

    // Check whether the logged-in user is the applicant or the job's recruiter.
    private void checkInterviewAccess(Interview interview, String email) {

        Application application = interview.getApplication();

        String applicantEmail = application.getApplicant().getEmail();

        String recruiterEmail = application.getJob()
                .getCompany()
                .getRecruiter()
                .getEmail();

        if (!applicantEmail.equals(email) &&
                !recruiterEmail.equals(email)) {

            throw new AccessDeniedException(
                    "You are not authorized to access this interview");
        }
    }

    // Find an application using its ID.
    private Application getApplication(Long applicationId) {

        return applicationRepo.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with Id: " + applicationId));
    }

    // Find an interview using its ID.
    private Interview getInterview(Long id) {

        return interviewRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Interview not found with Id: " + id));
    }

    @Override
    public InterviewResponse scheduleInterview(
            Interview interview, String email) {

        if (interview.getApplication() == null ||
                interview.getApplication().getId() == null) {

            throw new ResourceNotFoundException(
                    "Application ID is required");
        }

        Application application =
                getApplication(interview.getApplication().getId());

        // Only the recruiter who owns the job can schedule an interview.
        checkRecruiterOwnership(application, email);

        interview.setApplication(application);

        Interview savedInterview = interviewRepo.save(interview);

        return convertToResponse(savedInterview);
    }

    @Override
    public List<InterviewResponse> getAllInterviews(String email) {

        return interviewRepo
                .findByApplication_Job_Company_Recruiter_Email(email)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public InterviewResponse getInterviewById(Long id, String email) {

        Interview interview = getInterview(id);

        // Applicant or owning recruiter can view it.
        checkInterviewAccess(interview, email);

        return convertToResponse(interview);
    }

    @Override
    public List<InterviewResponse> getInterviewsByApplication(
            Long applicationId, String email) {

        Application application = getApplication(applicationId);

        String applicantEmail = application.getApplicant().getEmail();

        String recruiterEmail = application.getJob()
                .getCompany()
                .getRecruiter()
                .getEmail();

        if (!applicantEmail.equals(email) &&
                !recruiterEmail.equals(email)) {

            throw new AccessDeniedException(
                    "You are not authorized to view these interviews");
        }

        return interviewRepo.findByApplicationId(applicationId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public InterviewResponse updateInterview(
            Interview interview, Long id, String email) {

        Interview interviewDB = getInterview(id);

        // Check ownership of the existing interview.
        checkRecruiterOwnership(interviewDB.getApplication(), email);

        if (interview.getApplication() == null ||
                interview.getApplication().getId() == null) {

            throw new ResourceNotFoundException(
                    "Application ID is required");
        }

        // Check ownership of the application being assigned.
        Application application =
                getApplication(interview.getApplication().getId());

        checkRecruiterOwnership(application, email);

        interviewDB.setApplication(application);
        interviewDB.setInterviewDate(interview.getInterviewDate());
        interviewDB.setInterviewMode(interview.getInterviewMode());
        interviewDB.setMeetingLink(interview.getMeetingLink());
        interviewDB.setStatus(interview.getStatus());

        Interview updatedInterview = interviewRepo.save(interviewDB);

        return convertToResponse(updatedInterview);
    }

    @Override
    public InterviewResponse updateInterviewStatus(
            Long id, String status, String email) {

        Interview interviewDB = getInterview(id);

        checkRecruiterOwnership(interviewDB.getApplication(), email);

        interviewDB.setStatus(status);

        Interview updatedInterview = interviewRepo.save(interviewDB);

        return convertToResponse(updatedInterview);
    }

    @Override
    public void deleteInterview(Long id, String email) {

        Interview interviewDB = getInterview(id);

        checkRecruiterOwnership(interviewDB.getApplication(), email);

        interviewRepo.delete(interviewDB);
    }
}