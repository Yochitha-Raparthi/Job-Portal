//package com.application.job.portal.repo;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.application.job.portal.model.Interview;
//
//public interface InterviewRepository extends JpaRepository<Interview,Long>{
//   List<Interview>findByApplicationId(Long applicationId);
//}


package com.application.job.portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.application.job.portal.model.Interview;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplicationId(Long applicationId);

    // Interviews for jobs owned by a recruiter
    List<Interview> findByApplication_Job_Company_Recruiter_Email(String email);

    // Interviews belonging to a job seeker's applications
    List<Interview> findByApplication_Applicant_Email(String email);
}
