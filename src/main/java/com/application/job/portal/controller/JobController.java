//package com.application.job.portal.controller;
//
//import java.util.List;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.application.job.portal.model.Job;
//import com.application.job.portal.service.JobService;
//
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/job")
//public class JobController {
//	
//	
//	private final JobService jobService;
//	public JobController(JobService jobService) {
//		this.jobService= jobService;
//	}
//	
//	@PreAuthorize("hasRole('RECRUITER')")
//	@PostMapping("/create")
//	Job createJob(@Valid @RequestBody Job j) {
//		return jobService.createJob(j);
//	}
//
//	@PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
//	@GetMapping("/getAll")
//	List<Job>getAllJobs(){
//		return jobService.getAllJobs();
//	}
//	
//	@PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
//	@GetMapping("/getJobById/{Id}")
//	Job getJobById(@PathVariable Long Id) {
//		return jobService.getJobById(Id);
//	}
//	
//	@PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
//	@GetMapping("/getJobByCompany/{companyId}")
//	List<Job> getJobsByCompany(@PathVariable Long companyId){
//		return jobService.getJobsByCompany(companyId);
//	}
//	
//	@PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
//	@GetMapping("/getJobByTitle/{title}")
//	List<Job> searchJobsByTitle(@PathVariable String title){
//		return jobService.searchJobsByTitle(title);
//	}
//	
//	@PreAuthorize("hasRole('RECRUITER')")
//	@PutMapping("/updateJob/{Id}")
//	Job updateJob(@Valid @RequestBody Job j, @PathVariable Long Id) {
//		return jobService.updateJob(j, Id);
//	}
//	
//	@PreAuthorize("hasRole('RECRUITER')")
//	@DeleteMapping("/deleteJob/{Id}")
//	String deleteJob(@PathVariable Long Id) {
//		jobService.deleteJob(Id);
//		return "Job "+Id+" Deleted";
//	}
//}


package com.application.job.portal.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.application.job.portal.model.Job;
import com.application.job.portal.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/create")
    public Job createJob(
            @Valid @RequestBody Job j,
            Authentication authentication) {

        return jobService.createJob(j, authentication.getName());
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getAll")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getJobById/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getJobByCompany/{companyId}")
    public List<Job> getJobsByCompany(@PathVariable Long companyId) {
        return jobService.getJobsByCompany(companyId);
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getJobByTitle/{title}")
    public List<Job> searchJobsByTitle(@PathVariable String title) {
        return jobService.searchJobsByTitle(title);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/updateJob/{id}")
    public Job updateJob(
            @Valid @RequestBody Job j,
            @PathVariable Long id,
            Authentication authentication) {

        return jobService.updateJob(j, id, authentication.getName());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/deleteJob/{id}")
    public String deleteJob(
            @PathVariable Long id,
            Authentication authentication) {

        jobService.deleteJob(id, authentication.getName());
        return "Job " + id + " Deleted";
    }
}
