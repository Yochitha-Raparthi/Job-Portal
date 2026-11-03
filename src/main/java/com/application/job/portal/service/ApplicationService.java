package com.application.job.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.model.Application;


public interface ApplicationService {

	Application applyForJob(Application a);
	List<Application>getAllApplications();
	Application getApplicationById(Long Id);
	List<Application>getApplicationsByUser(Long userId);
	List<Application>getApplicationsByJob(Long jobId);
	Application updateApplicationStatus(Long Id,String status);
	void withdrawApplication(Long Id);
}
