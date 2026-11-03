package com.application.job.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Application;
import com.application.job.portal.repo.ApplicationRepository;
import com.application.job.portal.repo.JobRepository;
import com.application.job.portal.repo.UserRepository;

@Service
public class ApplicationServiceImpl implements ApplicationService{
	
	private final ApplicationRepository applicationRepo;
	private final UserRepository userRepo;
	private final JobRepository jobRepo;

	public ApplicationServiceImpl(
	        ApplicationRepository applicationRepo,
	        UserRepository userRepo,
	        JobRepository jobRepo) {

	    this.applicationRepo = applicationRepo;
	    this.userRepo = userRepo;
	    this.jobRepo = jobRepo;
	}
	
	private void validateApplication(Application a) {

	    if (a.getApplicant() == null ||
	        a.getApplicant().getId() == null ||
	        !userRepo.existsById(a.getApplicant().getId())) {
	        throw new ResourceNotFoundException("Applicant not found");
	    }

	    if (a.getJob() == null ||
	        a.getJob().getId() == null ||
	        !jobRepo.existsById(a.getJob().getId())) {
	        throw new ResourceNotFoundException("Job not found");
	    }
	}
	
	@Override
	public Application applyForJob(Application a) {
        validateApplication(a);
		a.setAppliedAt(LocalDateTime.now());
		return applicationRepo.save(a);
	}

	@Override
	public List<Application> getAllApplications() {
		return applicationRepo.findAll();
	}

	@Override
	public Application getApplicationById(Long Id) {
		return applicationRepo.findById(Id).orElseThrow(()->
                   new ResourceNotFoundException("Application not found with ID: " + Id));
	}

	@Override
	public List<Application> getApplicationsByUser(Long userId) {
		return applicationRepo.findByApplicantId(userId);
	}

	@Override
	public List<Application> getApplicationsByJob(Long jobId) {
		return applicationRepo.findByJobId(jobId);
	}

	@Override
	public Application updateApplicationStatus(Long Id, String status) {
		Application applicationDB=applicationRepo.findById(Id).orElseThrow(()->
                    new ResourceNotFoundException("Application not found with ID: " + Id));
		applicationDB.setStatus(status);
		return applicationRepo.save(applicationDB);
	}

	@Override
	public void withdrawApplication(Long Id) {
		Application applicationDB=applicationRepo.findById(Id).orElseThrow(()->
        new ResourceNotFoundException("Application not found with ID: " + Id));
		applicationRepo.delete(applicationDB);
		
	}

}
