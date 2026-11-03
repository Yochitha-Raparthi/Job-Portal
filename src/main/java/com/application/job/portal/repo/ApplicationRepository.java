package com.application.job.portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.job.portal.model.Application;

public interface ApplicationRepository extends JpaRepository<Application,Long>{
	List<Application>findByApplicantId(Long userId);
	List<Application>findByJobId(Long jobId);
}
