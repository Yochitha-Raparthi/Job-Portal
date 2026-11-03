package com.application.job.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Interview;
import com.application.job.portal.repo.ApplicationRepository;
import com.application.job.portal.repo.InterviewRepository;

@Service
public class InterviewServiceImpl implements InterviewService{
	
	private final InterviewRepository interviewRepo;
	private final ApplicationRepository applicationRepo;

	public InterviewServiceImpl(
	        InterviewRepository interviewRepo,
	        ApplicationRepository applicationRepo) {

	    this.interviewRepo = interviewRepo;
	    this.applicationRepo = applicationRepo;
	}
	
	private void validateApplication(Interview i) {

	    if (i.getApplication() == null ||
	        i.getApplication().getId() == null ||
	        !applicationRepo.existsById(i.getApplication().getId())) {
	        throw new ResourceNotFoundException("Application not found");
	    }
	}
	
	@Override
	public Interview scheduleInterview(Interview i) {
		validateApplication(i);
		return interviewRepo.save(i);
	}
	@Override
	public List<Interview> getAllInterviews() {
		return interviewRepo.findAll();
	}
	@Override
	public Interview getInterviewById(Long Id) {
		return interviewRepo.findById(Id).orElseThrow(()->
		                           new ResourceNotFoundException("Interview not found with Id: "+Id));
	}
	@Override
	public List<Interview> getInterviewsByApplication(Long applicationId) {
		return interviewRepo.findByApplicationId(applicationId);
	}
	@Override
	public Interview updateInterview(Interview i, Long Id) {
		Interview interviewDB=interviewRepo.findById(Id).orElseThrow(()->
                             new ResourceNotFoundException("Interview not found with Id: "+Id));
		validateApplication(i);
		interviewDB.setApplication(i.getApplication());
		interviewDB.setInterviewDate(i.getInterviewDate());
		interviewDB.setInterviewMode(i.getInterviewMode());
		interviewDB.setMeetingLink(i.getMeetingLink());
		interviewDB.setStatus(i.getStatus());
		return interviewRepo.save(interviewDB);
	}
	@Override
	public Interview updateInterviewStatus(Long Id, String status) {
		Interview interviewDB=interviewRepo.findById(Id).orElseThrow(()->
                     new ResourceNotFoundException("Interview not found with Id: "+Id));
		interviewDB.setStatus(status);
		return interviewRepo.save(interviewDB);
	}
	@Override
	public void deleteInterview(Long Id) {
		Interview interviewDB=interviewRepo.findById(Id).orElseThrow(()->
        new ResourceNotFoundException("Interview not found with Id: "+Id));
		interviewRepo.delete(interviewDB);
		
	}
	

}
