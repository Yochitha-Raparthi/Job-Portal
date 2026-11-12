package com.application.job.portal.dto;

import java.time.LocalDateTime;

public record InterviewResponse(
        Long id,
        Long applicationId,
        Long applicantId,
        String applicantName,
        Long jobId,
        String jobTitle,
        LocalDateTime interviewDate,
        String interviewMode,
        String meetingLink,
        String status
) {}