package com.application.job.portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.job.portal.model.Company;

public interface CompanyRepository extends JpaRepository<Company,Long>{

	List<Company>findByRecruiterId(Long recruiterId);
}
