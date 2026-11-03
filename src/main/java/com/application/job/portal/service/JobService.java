package com.application.job.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.model.Job;


public interface JobService {

	Job createJob(Job j);
	List<Job>getAllJobs();
	Job getJobById(Long Id);
	List<Job>getJobsByCompany(Long companyId);
	List<Job> searchJobsByTitle(String title);
	Job updateJob(Job j, Long Id);
	void deleteJob(Long Id);
}
