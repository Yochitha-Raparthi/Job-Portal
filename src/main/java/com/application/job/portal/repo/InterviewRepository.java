package com.application.job.portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.job.portal.model.Interview;

public interface InterviewRepository extends JpaRepository<Interview,Long>{
   List<Interview>findByApplicationId(Long applicationId);
}
