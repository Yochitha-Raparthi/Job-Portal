//package com.application.job.portal.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.application.job.portal.exception.ResourceNotFoundException;
//import com.application.job.portal.model.Interview;
//import com.application.job.portal.repo.ApplicationRepository;
//import com.application.job.portal.repo.InterviewRepository;
//
//@Service
//public class InterviewServiceImpl implements InterviewService{
//	
//	private final InterviewRepository interviewRepo;
//	private final ApplicationRepository applicationRepo;
//
//	public InterviewServiceImpl(
//	        InterviewRepository interviewRepo,
//	        ApplicationRepository applicationRepo) {
//
//	    this.interviewRepo = interviewRepo;
//	    this.applicationRepo = applicationRepo;
//	}
//	
//	private void validateApplication(Interview i) {
//
//	    if (i.getApplication() == null ||
//	        i.getApplication().getId() == null ||
//	        !applicationRepo.existsById(i.getApplication().getId())) {
//	        throw new ResourceNotFoundException("Application not found");
//	    }
//	}
//	
//	@Override
//	public Interview scheduleInterview(Interview i) {
//		validateApplication(i);
//		return interviewRepo.save(i);
//	}
//	@Override
//	public List<Interview> getAllInterviews() {
//		return interviewRepo.findAll();
//	}
//	@Override
//	public Interview getInterviewById(Long Id) {
//		return interviewRepo.findById(Id).orElseThrow(()->
//		                           new ResourceNotFoundException("Interview not found with Id: "+Id));
//	}
//	@Override
//	public List<Interview> getInterviewsByApplication(Long applicationId) {
//		return interviewRepo.findByApplicationId(applicationId);
//	}
//	@Override
//	public Interview updateInterview(Interview i, Long Id) {
//		Interview interviewDB=interviewRepo.findById(Id).orElseThrow(()->
//                             new ResourceNotFoundException("Interview not found with Id: "+Id));
//		validateApplication(i);
//		interviewDB.setApplication(i.getApplication());
//		interviewDB.setInterviewDate(i.getInterviewDate());
//		interviewDB.setInterviewMode(i.getInterviewMode());
//		interviewDB.setMeetingLink(i.getMeetingLink());
//		interviewDB.setStatus(i.getStatus());
//		return interviewRepo.save(interviewDB);
//	}
//	@Override
//	public Interview updateInterviewStatus(Long Id, String status) {
//		Interview interviewDB=interviewRepo.findById(Id).orElseThrow(()->
//                     new ResourceNotFoundException("Interview not found with Id: "+Id));
//		interviewDB.setStatus(status);
//		return interviewRepo.save(interviewDB);
//	}
//	@Override
//	public void deleteInterview(Long Id) {
//		Interview interviewDB=interviewRepo.findById(Id).orElseThrow(()->
//        new ResourceNotFoundException("Interview not found with Id: "+Id));
//		interviewRepo.delete(interviewDB);
//		
//	}
//	
//
//}


package com.application.job.portal.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Application;
import com.application.job.portal.model.Interview;
import com.application.job.portal.repo.ApplicationRepository;
import com.application.job.portal.repo.InterviewRepository;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepo;
    private final ApplicationRepository applicationRepo;

    public InterviewServiceImpl(
            InterviewRepository interviewRepo,
            ApplicationRepository applicationRepo) {

        this.interviewRepo = interviewRepo;
        this.applicationRepo = applicationRepo;
    }

    // Check whether the logged-in recruiter owns the job
    private void checkRecruiterOwnership(Application application, String email) {

        String recruiterEmail = application.getJob()
                .getCompany()
                .getRecruiter()
                .getEmail();

        if (!recruiterEmail.equals(email)) {
            throw new AccessDeniedException(
                    "You are not authorized to access this interview");
        }
    }

    // Check whether the logged-in user is the applicant or the job's recruiter
    private void checkInterviewAccess(Interview interview, String email) {

        Application application = interview.getApplication();

        String applicantEmail = application.getApplicant().getEmail();
        String recruiterEmail = application.getJob()
                .getCompany()
                .getRecruiter()
                .getEmail();

        if (!applicantEmail.equals(email) && !recruiterEmail.equals(email)) {
            throw new AccessDeniedException(
                    "You are not authorized to access this interview");
        }
    }

    // Find an application using its ID
    private Application getApplication(Long applicationId) {

        return applicationRepo.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with Id: " + applicationId));
    }

    // Find an interview using its ID
    private Interview getInterview(Long id) {

        return interviewRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Interview not found with Id: " + id));
    }

    @Override
    public Interview scheduleInterview(Interview interview, String email) {

        if (interview.getApplication() == null ||
                interview.getApplication().getId() == null) {
            throw new ResourceNotFoundException("Application ID is required");
        }

        // Load the actual application from the database
        Application application =
                getApplication(interview.getApplication().getId());

        // Only the recruiter who owns the job can schedule an interview
        checkRecruiterOwnership(application, email);

        interview.setApplication(application);

        return interviewRepo.save(interview);
    }

    @Override
    public List<Interview> getAllInterviews(String email) {

        // Return only interviews for this recruiter's jobs
        return interviewRepo
                .findByApplication_Job_Company_Recruiter_Email(email);
    }

    @Override
    public Interview getInterviewById(Long id, String email) {

        Interview interview = getInterview(id);

        // Applicant or owning recruiter can view it
        checkInterviewAccess(interview, email);

        return interview;
    }

    @Override
    public List<Interview> getInterviewsByApplication(
            Long applicationId, String email) {

        Application application = getApplication(applicationId);

        String applicantEmail = application.getApplicant().getEmail();
        String recruiterEmail = application.getJob()
                .getCompany()
                .getRecruiter()
                .getEmail();

        if (!applicantEmail.equals(email) &&
                !recruiterEmail.equals(email)) {
            throw new AccessDeniedException(
                    "You are not authorized to view these interviews");
        }

        return interviewRepo.findByApplicationId(applicationId);
    }

    @Override
    public Interview updateInterview(
            Interview interview, Long id, String email) {

        Interview interviewDB = getInterview(id);

        // Check ownership of the existing interview
        checkRecruiterOwnership(interviewDB.getApplication(), email);

        if (interview.getApplication() == null ||
                interview.getApplication().getId() == null) {
            throw new ResourceNotFoundException("Application ID is required");
        }

        // Check ownership of the application being assigned
        Application application =
                getApplication(interview.getApplication().getId());

        checkRecruiterOwnership(application, email);

        interviewDB.setApplication(application);
        interviewDB.setInterviewDate(interview.getInterviewDate());
        interviewDB.setInterviewMode(interview.getInterviewMode());
        interviewDB.setMeetingLink(interview.getMeetingLink());
        interviewDB.setStatus(interview.getStatus());

        return interviewRepo.save(interviewDB);
    }

    @Override
    public Interview updateInterviewStatus(
            Long id, String status, String email) {

        Interview interviewDB = getInterview(id);

        checkRecruiterOwnership(interviewDB.getApplication(), email);

        interviewDB.setStatus(status);

        return interviewRepo.save(interviewDB);
    }

    @Override
    public void deleteInterview(Long id, String email) {

        Interview interviewDB = getInterview(id);

        checkRecruiterOwnership(interviewDB.getApplication(), email);

        interviewRepo.delete(interviewDB);
    }
}
