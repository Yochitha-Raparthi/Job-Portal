package com.application.job.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.model.Interview;


public interface InterviewService {

	Interview scheduleInterview(Interview i);
	List<Interview>getAllInterviews();
	Interview getInterviewById(Long Id);
	List<Interview>getInterviewsByApplication(Long applicationId);
	Interview updateInterview(Interview i, Long Id);
	Interview updateInterviewStatus(Long Id, String status);
	void deleteInterview(Long Id);
}
