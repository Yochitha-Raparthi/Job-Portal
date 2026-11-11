//package com.application.job.portal.repo;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.application.job.portal.model.Application;
//
//public interface ApplicationRepository extends JpaRepository<Application,Long>{
//	List<Application>findByApplicantId(Long userId);
//	List<Application>findByJobId(Long jobId);
//}

package com.application.job.portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.job.portal.model.Application;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByApplicantId(Long userId);

    List<Application> findByJobId(Long jobId);

    // Find applications belonging to a logged-in Job Seeker.
    List<Application> findByApplicant_Email(String email);

    // Find applications for jobs belonging to a logged-in Recruiter.
    List<Application> findByJob_Company_Recruiter_Email(String email);

    // Find one application only if its job belongs to this recruiter.
    List<Application> findByJob_IdAndJob_Company_Recruiter_Email(
            Long jobId, String email);
}
