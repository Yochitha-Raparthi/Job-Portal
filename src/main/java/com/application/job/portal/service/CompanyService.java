package com.application.job.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.model.Company;


public interface CompanyService {
	
	List<Company>getAllCompanies();
	
	Company getCompanyById(Long Id);
	
	Company createCompany(Company c, String email);

	List<Company> getMyCompanies(String email);

	Company updateCompany(Company c, Long id, String email);

	void deleteCompany(Long id, String email);
}
