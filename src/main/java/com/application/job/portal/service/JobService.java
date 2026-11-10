package com.application.job.portal.service;

import java.util.List;

import com.application.job.portal.model.Job;

public interface JobService {
	List<Job> getAllJobs();

	Job getJobById(Long Id);

	List<Job> getJobsByCompany(Long companyId);

	List<Job> searchJobsByTitle(String title);

	Job createJob(Job j, String email);

	Job updateJob(Job j, Long id, String email);

	void deleteJob(Long id, String email);
}
