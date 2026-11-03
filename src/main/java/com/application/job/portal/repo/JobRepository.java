package com.application.job.portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.job.portal.model.Job;

public interface JobRepository extends JpaRepository<Job,Long>{

	List<Job>findByCompanyId(Long companyId);
	List<Job>findByTitleContainingIgnoreCase(String title);
}
