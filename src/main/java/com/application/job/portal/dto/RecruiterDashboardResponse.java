package com.application.job.portal.dto;

public record RecruiterDashboardResponse(
        long totalCompanies,
        long totalJobs,
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