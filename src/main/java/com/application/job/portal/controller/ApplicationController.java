package com.application.job.portal.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.job.portal.model.Application;
import com.application.job.portal.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/application")
public class ApplicationController {

	private final ApplicationService applicationService;
	public ApplicationController (ApplicationService applicationService) {
		this.applicationService=applicationService;
	}
	
	@PreAuthorize("hasRole('JOB_SEEKER')")
	@PostMapping("/create")
	Application applyForJob(@Valid @RequestBody Application a) {
		return applicationService.applyForJob(a);
	}
	
	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping("/allApplications")
	List<Application> getAllApplications(){
		return applicationService.getAllApplications();
	}
	
	@PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
	@GetMapping("/getApplicationById/{Id}")
	Application getApplicationById(@PathVariable Long Id) {
		return applicationService.getApplicationById(Id);
	}
	
	@PreAuthorize("hasRole('JOB_SEEKER')")
	@GetMapping("/getApplicationsByUser/{userId}")
	List<Application> getApplicationsByUser(@PathVariable Long userId){
		return applicationService.getApplicationsByUser(userId);
	}
	
	@PreAuthorize("hasRole('RECRUITER')")
	@GetMapping("/getApplicationsByJob/{jobId}")
	List<Application> getApplicationsByJob(@PathVariable Long jobId){
		return applicationService.getApplicationsByJob(jobId);
	}
	
	@PreAuthorize("hasRole('RECRUITER')")
	@PatchMapping("/updateApplicationStatus/{Id}")
	Application updateApplicationStatus(@PathVariable Long Id, @RequestParam String status) {
		return applicationService.updateApplicationStatus(Id, status);
	}
	
	@PreAuthorize("hasRole('JOB_SEEKER')")
	@DeleteMapping("/withdraw/{Id}")
	String withdrawApplication(@PathVariable Long Id) {
		 applicationService.withdrawApplication(Id);
		 return "Application "+Id+" Withdraw";
	}
}
