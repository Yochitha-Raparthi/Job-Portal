package com.application.job.portal.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.application.job.portal.dto.InterviewResponse;
import com.application.job.portal.model.Interview;
import com.application.job.portal.service.InterviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    // 1. Schedule an interview.
    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/schedule")
    public InterviewResponse scheduleInterview(
            @Valid @RequestBody Interview interview,
            Authentication authentication) {

        return interviewService.scheduleInterview(
                interview, authentication.getName());
    }

    // 2. Get interviews belonging to the logged-in recruiter.
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/all")
    public List<InterviewResponse> getAllInterviews(
            Authentication authentication) {

        return interviewService.getAllInterviews(
                authentication.getName());
    }

    // 3. Get an interview by ID.
    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/{id}")
    public InterviewResponse getInterviewById(
            @PathVariable Long id,
            Authentication authentication) {

        return interviewService.getInterviewById(
                id, authentication.getName());
    }

    // 4. Get interviews for an application.
    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/application/{applicationId}")
    public List<InterviewResponse> getInterviewsByApplication(
            @PathVariable Long applicationId,
            Authentication authentication) {

        return interviewService.getInterviewsByApplication(
                applicationId, authentication.getName());
    }

    // 5. Update interview details.
    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/update/{id}")
    public InterviewResponse updateInterview(
            @Valid @RequestBody Interview interview,
            @PathVariable Long id,
            Authentication authentication) {

        return interviewService.updateInterview(
                interview, id, authentication.getName());
    }

    // 6. Update interview status.
    @PreAuthorize("hasRole('RECRUITER')")
    @PatchMapping("/updateStatus/{id}")
    public InterviewResponse updateInterviewStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {

        return interviewService.updateInterviewStatus(
                id, status, authentication.getName());
    }

    // 7. Delete an interview.
    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/delete/{id}")
    public String deleteInterview(
            @PathVariable Long id,
            Authentication authentication) {

        interviewService.deleteInterview(
                id, authentication.getName());

        return "Interview " + id + " deleted successfully";
    }
}