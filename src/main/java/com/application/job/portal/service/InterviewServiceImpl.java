
package com.application.job.portal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    private static final String SCHEDULED = "SCHEDULED";
    private static final String RESCHEDULED = "RESCHEDULED";
    private static final String COMPLETED = "COMPLETED";
    private static final String CANCELLED = "CANCELLED";

    private static final Set<String> ALLOWED_STATUSES =
            Set.of(SCHEDULED, RESCHEDULED, COMPLETED, CANCELLED);

    private final InterviewRepository interviewRepo;
    private final ApplicationRepository applicationRepo;

    public InterviewServiceImpl(
            InterviewRepository interviewRepo,
            ApplicationRepository applicationRepo) {

        this.interviewRepo = interviewRepo;
        this.applicationRepo = applicationRepo;
    }

    // Convert Interview entity into response DTO.
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

    // Check whether the recruiter owns the job.
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

    // Check whether applicant or recruiter can view interview.
    private void checkInterviewAccess(
            Interview interview, String email) {

        Application application = interview.getApplication();

        String applicantEmail =
                application.getApplicant().getEmail();

        String recruiterEmail = application.getJob()
                .getCompany()
                .getRecruiter()
                .getEmail();

        if (!applicantEmail.equals(email)
                && !recruiterEmail.equals(email)) {

            throw new AccessDeniedException(
                    "You are not authorized to access this interview");
        }
    }

    // Find application by ID.
    private Application getApplication(Long applicationId) {

        return applicationRepo.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with Id: "
                                        + applicationId));
    }

    // Find interview by ID.
    private Interview getInterview(Long id) {

        return interviewRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Interview not found with Id: " + id));
    }

    // Validate application eligibility.
    private void validateShortlistedApplication(
            Application application) {

        if (!"SHORTLISTED".equals(application.getStatus())) {
            throw new IllegalArgumentException(
                    "Only shortlisted applications can be scheduled for interviews");
        }
    }

    // Validate interview date.
    private void validateFutureDate(LocalDateTime date) {

        if (date == null || !date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Interview date must be in the future");
        }
    }

    // Prevent changes to completed/cancelled interviews.
    private void validateEditable(Interview interview) {

        if (COMPLETED.equals(interview.getStatus())
                || CANCELLED.equals(interview.getStatus())) {

            throw new IllegalStateException(
                    "Completed or cancelled interviews cannot be modified");
        }
    }

    // 1. Schedule interview.
    @Override
    public InterviewResponse scheduleInterview(
            Interview interview, String email) {

        if (interview.getApplication() == null
                || interview.getApplication().getId() == null) {

            throw new IllegalArgumentException(
                    "Application ID is required");
        }

        Application application =
                getApplication(interview.getApplication().getId());

        // Verify recruiter ownership.
        checkRecruiterOwnership(application, email);

        // Only shortlisted candidates can be interviewed.
        validateShortlistedApplication(application);

        // Interview must be in the future.
        validateFutureDate(interview.getInterviewDate());

        interview.setApplication(application);

        // Always assign initial status automatically.
        interview.setStatus(SCHEDULED);

        Interview savedInterview = interviewRepo.save(interview);

        return convertToResponse(savedInterview);
    }

    // 2. Get recruiter's interviews.
    @Override
    public List<InterviewResponse> getAllInterviews(String email) {

        return interviewRepo
                .findByApplication_Job_Company_Recruiter_Email(email)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 3. Get interview by ID.
    @Override
    public InterviewResponse getInterviewById(
            Long id, String email) {

        Interview interview = getInterview(id);

        checkInterviewAccess(interview, email);

        return convertToResponse(interview);
    }

    // 4. Get interviews for an application.
    @Override
    public List<InterviewResponse> getInterviewsByApplication(
            Long applicationId, String email) {

        Application application = getApplication(applicationId);

        String applicantEmail =
                application.getApplicant().getEmail();

        String recruiterEmail = application.getJob()
                .getCompany()
                .getRecruiter()
                .getEmail();

        if (!applicantEmail.equals(email)
                && !recruiterEmail.equals(email)) {

            throw new AccessDeniedException(
                    "You are not authorized to view these interviews");
        }

        return interviewRepo.findByApplicationId(applicationId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // 5. Update interview details.
    @Override
    public InterviewResponse updateInterview(
            Interview interview, Long id, String email) {

        Interview interviewDB = getInterview(id);

        // Verify recruiter owns the existing interview.
        checkRecruiterOwnership(interviewDB.getApplication(), email);

        // Prevent editing completed/cancelled interviews.
        validateEditable(interviewDB);

        if (interview.getApplication() == null
                || interview.getApplication().getId() == null) {

            throw new IllegalArgumentException(
                    "Application ID is required");
        }

        // Prevent changing the interview's application.
        if (!interviewDB.getApplication().getId()
                .equals(interview.getApplication().getId())) {

            throw new IllegalArgumentException(
                    "Cannot change the application of an existing interview");
        }

        validateFutureDate(interview.getInterviewDate());

        // Check whether date or time changed.
        boolean dateChanged =
                !interviewDB.getInterviewDate()
                        .equals(interview.getInterviewDate());

        // Update interview details.
        interviewDB.setInterviewDate(interview.getInterviewDate());
        interviewDB.setInterviewMode(interview.getInterviewMode());
        interviewDB.setMeetingLink(interview.getMeetingLink());

        // Assign RESCHEDULED only when date/time changes.
        if (dateChanged) {
            interviewDB.setStatus(RESCHEDULED);
        }

        // Do not copy status from request body.
        Interview updatedInterview =
                interviewRepo.save(interviewDB);

        return convertToResponse(updatedInterview);
    }

    // 6. Update interview status.
    @Override
    public InterviewResponse updateInterviewStatus(
            Long id, String status, String email) {

        Interview interviewDB = getInterview(id);

        checkRecruiterOwnership(interviewDB.getApplication(), email);

        if (status == null || !ALLOWED_STATUSES.contains(status)) {
            throw new IllegalArgumentException(
                    "Invalid interview status: " + status);
        }

        // RESCHEDULED is assigned only by changing date/time.
        if (RESCHEDULED.equals(status)) {
            throw new IllegalArgumentException(
                    "Change the interview date to reschedule it");
        }

        // Prevent manually assigning SCHEDULED.
        if (SCHEDULED.equals(status)) {
            throw new IllegalArgumentException(
                    "SCHEDULED is assigned automatically");
        }

        // Prevent changing a finalized interview.
        if (COMPLETED.equals(interviewDB.getStatus())
                || CANCELLED.equals(interviewDB.getStatus())) {

            throw new IllegalStateException(
                    "Cannot change the status of a completed or cancelled interview");
        }

        // Recruiter can mark interview COMPLETED or CANCELLED.
        interviewDB.setStatus(status);

        Interview updatedInterview =
                interviewRepo.save(interviewDB);

        return convertToResponse(updatedInterview);
    }

    // 7. Delete interview.
    @Override
    public void deleteInterview(Long id, String email) {

        Interview interviewDB = getInterview(id);

        checkRecruiterOwnership(interviewDB.getApplication(), email);

        interviewRepo.delete(interviewDB);
    }
}
