package com.application.job.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Job;
import com.application.job.portal.repo.CompanyRepository;
import com.application.job.portal.repo.JobRepository;

@Service
public class JobServiceImpl implements JobService{

	private final JobRepository jobRepo;
	private final CompanyRepository companyRepo;
	public JobServiceImpl(
	        JobRepository jobRepo,
	        CompanyRepository companyRepo) {
	    this.jobRepo = jobRepo;
	    this.companyRepo = companyRepo;
	}
	
	@Override
	public Job createJob(Job j) {

	    if (j.getCompany() == null ||
	        j.getCompany().getId() == null ||
	        !companyRepo.existsById(j.getCompany().getId())) {

	        throw new ResourceNotFoundException("Company not found");
	    }

	    j.setPostedAt(LocalDateTime.now());

	    return jobRepo.save(j);
	}

	@Override
	public List<Job> getAllJobs() {
		return jobRepo.findAll();
	}

	@Override
	public Job getJobById(Long Id) {
		return jobRepo.findById(Id).orElseThrow(()->
		                              new ResourceNotFoundException("Job not found with Id :"+Id));
	}

	@Override
	public List<Job> getJobsByCompany(Long companyId) {
		return jobRepo.findByCompanyId(companyId);
	}

	@Override
	public List<Job> searchJobsByTitle(String title) {
		return jobRepo.findByTitleContainingIgnoreCase(title);
	}

	@Override
	public Job updateJob(Job j, Long Id) {
		Job jobDB=jobRepo.findById(Id).orElseThrow(()->
                        new ResourceNotFoundException("Job not found with Id :"+Id));
		  if (j.getCompany() == null ||
			        j.getCompany().getId() == null ||
			        !companyRepo.existsById(j.getCompany().getId())) {
			        throw new ResourceNotFoundException("Company not found");
			    }
		jobDB.setDescription(j.getDescription());
		jobDB.setJobType(j.getJobType());
		jobDB.setLocation(j.getLocation());
		jobDB.setSalary(j.getSalary());
		jobDB.setTitle(j.getTitle());
		jobDB.setCompany(j.getCompany());
		return jobRepo.save(jobDB);
	}

	@Override
	public void deleteJob(Long Id) {
		Job jobDB=jobRepo.findById(Id).orElseThrow(()->
        new ResourceNotFoundException("Job not found with Id :"+Id));
		jobRepo.delete(jobDB);
		
	}

	
}
