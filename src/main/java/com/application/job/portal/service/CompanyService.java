package com.application.job.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.model.Company;


public interface CompanyService {

	Company createCompany(Company c);
	List<Company>getAllCompanies();
	Company getCompanyById(Long Id);
	List<Company>getCompaniesByRecruiters(Long recruiterId);
	Company updateCompany(Company c,Long Id);
	void deleteCompany(Long Id);
}
