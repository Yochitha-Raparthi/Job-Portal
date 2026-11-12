package com.application.job.portal.service;

import java.util.List;

import com.application.job.portal.dto.InterviewResponse;
import com.application.job.portal.model.Interview;

public interface InterviewService {

    InterviewResponse scheduleInterview(Interview interview, String email);

    List<InterviewResponse> getAllInterviews(String email);

    InterviewResponse getInterviewById(Long id, String email);

    List<InterviewResponse> getInterviewsByApplication(
            Long applicationId, String email);

    InterviewResponse updateInterview(
            Interview interview, Long id, String email);

    InterviewResponse updateInterviewStatus(
            Long id, String status, String email);

    void deleteInterview(Long id, String email);
}