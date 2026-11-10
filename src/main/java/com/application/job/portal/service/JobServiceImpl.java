//package com.application.job.portal.service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.application.job.portal.exception.ResourceNotFoundException;
//import com.application.job.portal.model.Job;
//import com.application.job.portal.repo.CompanyRepository;
//import com.application.job.portal.repo.JobRepository;
//
//@Service
//public class JobServiceImpl implements JobService{
//
//	private final JobRepository jobRepo;
//	private final CompanyRepository companyRepo;
//	public JobServiceImpl(
//	        JobRepository jobRepo,
//	        CompanyRepository companyRepo) {
//	    this.jobRepo = jobRepo;
//	    this.companyRepo = companyRepo;
//	}
//	
//	@Override
//	public Job createJob(Job j) {
//
//	    if (j.getCompany() == null ||
//	        j.getCompany().getId() == null ||
//	        !companyRepo.existsById(j.getCompany().getId())) {
//
//	        throw new ResourceNotFoundException("Company not found");
//	    }
//
//	    j.setPostedAt(LocalDateTime.now());
//
//	    return jobRepo.save(j);
//	}
//
//	@Override
//	public List<Job> getAllJobs() {
//		return jobRepo.findAll();
//	}
//
//	@Override
//	public Job getJobById(Long Id) {
//		return jobRepo.findById(Id).orElseThrow(()->
//		                              new ResourceNotFoundException("Job not found with Id :"+Id));
//	}
//
//	@Override
//	public List<Job> getJobsByCompany(Long companyId) {
//		return jobRepo.findByCompanyId(companyId);
//	}
//
//	@Override
//	public List<Job> searchJobsByTitle(String title) {
//		return jobRepo.findByTitleContainingIgnoreCase(title);
//	}
//
//	@Override
//	public Job updateJob(Job j, Long Id) {
//		Job jobDB=jobRepo.findById(Id).orElseThrow(()->
//                        new ResourceNotFoundException("Job not found with Id :"+Id));
//		  if (j.getCompany() == null ||
//			        j.getCompany().getId() == null ||
//			        !companyRepo.existsById(j.getCompany().getId())) {
//			        throw new ResourceNotFoundException("Company not found");
//			    }
//		jobDB.setDescription(j.getDescription());
//		jobDB.setJobType(j.getJobType());
//		jobDB.setLocation(j.getLocation());
//		jobDB.setSalary(j.getSalary());
//		jobDB.setTitle(j.getTitle());
//		jobDB.setCompany(j.getCompany());
//		return jobRepo.save(jobDB);
//	}
//
//	@Override
//	public void deleteJob(Long Id) {
//		Job jobDB=jobRepo.findById(Id).orElseThrow(()->
//        new ResourceNotFoundException("Job not found with Id :"+Id));
//		jobRepo.delete(jobDB);
//		
//	}
//
//	
//}

package com.application.job.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Company;
import com.application.job.portal.model.Job;
import com.application.job.portal.repo.CompanyRepository;
import com.application.job.portal.repo.JobRepository;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepo;
    private final CompanyRepository companyRepo;

    public JobServiceImpl(
            JobRepository jobRepo,
            CompanyRepository companyRepo) {
        this.jobRepo = jobRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public Job createJob(Job j, String email) {

        if (j.getCompany() == null ||
                j.getCompany().getId() == null) {
            throw new ResourceNotFoundException("Company is required");
        }

        Company company = companyRepo.findById(j.getCompany().getId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Company not found"));

        // Check whether the logged-in recruiter owns this company.
        if (!company.getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot create a job for another recruiter's company");
        }

        j.setCompany(company);
        j.setPostedAt(LocalDateTime.now());

        return jobRepo.save(j);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Job not found with Id: " + id));
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
    public Job updateJob(Job j, Long id, String email) {

        Job jobDB = jobRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Job not found with Id: " + id));

        // First, check ownership of the existing job.
        if (!jobDB.getCompany().getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot update another recruiter's job");
        }

        if (j.getCompany() == null ||
                j.getCompany().getId() == null) {
            throw new ResourceNotFoundException("Company is required");
        }

        Company newCompany = companyRepo.findById(j.getCompany().getId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Company not found"));

        // The recruiter must also own the company receiving the job.
        if (!newCompany.getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot assign a job to another recruiter's company");
        }

        jobDB.setTitle(j.getTitle());
        jobDB.setDescription(j.getDescription());
        jobDB.setJobType(j.getJobType());
        jobDB.setLocation(j.getLocation());
        jobDB.setSalary(j.getSalary());
        jobDB.setCompany(newCompany);

        return jobRepo.save(jobDB);
    }

    @Override
    public void deleteJob(Long id, String email) {

        Job jobDB = jobRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Job not found with Id: " + id));

        if (!jobDB.getCompany().getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot delete another recruiter's job");
        }

        jobRepo.delete(jobDB);
    }
}
