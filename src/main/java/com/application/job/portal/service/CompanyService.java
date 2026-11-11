package com.application.job.portal.service;

import java.util.List;

import com.application.job.portal.dto.CompanyResponse;
import com.application.job.portal.model.Company;

public interface CompanyService {

    List<CompanyResponse> getAllCompanies();

    CompanyResponse getCompanyById(Long id);

    CompanyResponse createCompany(Company c, String email);

    List<CompanyResponse> getMyCompanies(String email);

    CompanyResponse updateCompany(Company c, Long id, String email);

    void deleteCompany(Long id, String email);
}