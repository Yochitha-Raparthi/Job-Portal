
package com.application.job.portal.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.job.portal.dto.RecruiterDashboardResponse;
import com.application.job.portal.dto.SeekerDashboardResponse;
import com.application.job.portal.model.Application;
import com.application.job.portal.model.Company;
import com.application.job.portal.model.Interview;
import com.application.job.portal.repo.ApplicationRepository;
import com.application.job.portal.repo.CompanyRepository;
import com.application.job.portal.repo.InterviewRepository;
import com.application.job.portal.repo.JobRepository;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final CompanyRepository companyRepo;
    private final JobRepository jobRepo;
    private final ApplicationRepository applicationRepo;
    private final InterviewRepository interviewRepo;

    public DashboardServiceImpl(
            CompanyRepository companyRepo,
            JobRepository jobRepo,
            ApplicationRepository applicationRepo,
            InterviewRepository interviewRepo) {

        this.companyRepo = companyRepo;
        this.jobRepo = jobRepo;
        this.applicationRepo = applicationRepo;
        this.interviewRepo = interviewRepo;
    }

    // Count records matching a particular status.
    private long countApplicationStatus(
            List<Application> applications, String status) {

        return applications.stream()
                .filter(a -> status.equalsIgnoreCase(a.getStatus()))
                .count();
    }

    private long countInterviewStatus(
            List<Interview> interviews, String status) {

        return interviews.stream()
                .filter(i -> status.equalsIgnoreCase(i.getStatus()))
                .count();
    }

    // RECRUITER DASHBOARD
    @Override
    public RecruiterDashboardResponse getRecruiterDashboard(
            String email) {

        // Fetch only companies owned by this recruiter.
        List<Company> companies =
                companyRepo.findByRecruiterEmail(email);

        // Get IDs of the recruiter's companies.
        Set<Long> companyIds = companies.stream()
                .map(Company::getId)
                .collect(Collectors.toSet());

        // Count jobs posted under those companies.
        long totalJobs = companyIds.stream()
                .mapToLong(companyId ->
                        jobRepo.findByCompanyId(companyId).size())
                .sum();

        // Fetch only applications for this recruiter's jobs.
        List<Application> applications =
                applicationRepo
                        .findByJob_Company_Recruiter_Email(email);

        // Fetch only interviews belonging to this recruiter.
        List<Interview> interviews =
                interviewRepo
                        .findByApplication_Job_Company_Recruiter_Email(email);

        return new RecruiterDashboardResponse(
                companies.size(),
                totalJobs,
                applications.size(),
                countApplicationStatus(applications, "PENDING"),
                countApplicationStatus(applications, "SHORTLISTED"),
                countApplicationStatus(applications, "REJECTED"),
                interviews.size(),
                countInterviewStatus(interviews, "SCHEDULED"),
                countInterviewStatus(interviews, "RESCHEDULED"),
                countInterviewStatus(interviews, "COMPLETED"),
                countInterviewStatus(interviews, "CANCELLED")
        );
    }

    // JOB SEEKER DASHBOARD
    @Override
    public SeekerDashboardResponse getSeekerDashboard(
            String email) {

        // Fetch only applications submitted by this job seeker.
        List<Application> applications =
                applicationRepo.findByApplicant_Email(email);

        // Fetch only interviews belonging to this job seeker.
        List<Interview> interviews =
                interviewRepo.findByApplication_Applicant_Email(email);

        return new SeekerDashboardResponse(
                applications.size(),
                countApplicationStatus(applications, "PENDING"),
                countApplicationStatus(applications, "SHORTLISTED"),
                countApplicationStatus(applications, "REJECTED"),
                interviews.size(),
                countInterviewStatus(interviews, "SCHEDULED"),
                countInterviewStatus(interviews, "RESCHEDULED"),
                countInterviewStatus(interviews, "COMPLETED"),
                countInterviewStatus(interviews, "CANCELLED")
        );
    }
}

