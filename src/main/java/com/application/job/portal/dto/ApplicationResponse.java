package com.application.job.portal.dto;

import java.time.LocalDateTime;

public record ApplicationResponse(
    Long id,
    Long applicantId,
    String applicantName,
    Long jobId,
    String jobTitle,
    String status,
    String resumeUrl,
    LocalDateTime appliedAt
) {}