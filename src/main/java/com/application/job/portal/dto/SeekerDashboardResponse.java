package com.application.job.portal.dto;

public record SeekerDashboardResponse(
        long totalApplications,
        long pendingApplications,
        long shortlistedApplications,
        long rejectedApplications,
        long totalInterviews,
        long scheduledInterviews,
        long rescheduledInterviews,
        long completedInterviews,
        long cancelledInterviews
) {
}