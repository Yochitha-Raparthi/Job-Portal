package com.application.job.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Company;
import com.application.job.portal.repo.CompanyRepository;
import com.application.job.portal.repo.UserRepository;

@Service
public class CompanyServiceImpl implements CompanyService{
	
	private final CompanyRepository companyRepo;
	private final UserRepository userRepo;

	public CompanyServiceImpl(
	        CompanyRepository companyRepository,
	        UserRepository userRepository) {
	    this.companyRepo = companyRepository;
	    this.userRepo = userRepository;
	}
	
	@Override
	public Company createCompany(Company c) {
		if (c.getRecruiter() == null ||
			    !userRepo.existsById(c.getRecruiter().getId())) {
			    throw new ResourceNotFoundException("Recruiter not found");
			}
		return companyRepo.save(c);
	}

	@Override
	public List<Company> getAllCompanies() {
		return companyRepo.findAll();
	}

	@Override
	public Company getCompanyById(Long Id) {
		return companyRepo.findById(Id).orElseThrow(()->
		                                   new ResourceNotFoundException("Company not found with Id: "+Id));
	}

	@Override
	public List<Company> getCompaniesByRecruiters(Long recruiterId) {
		return companyRepo.findByRecruiterId(recruiterId);
	}

	@Override
	public Company updateCompany(Company c, Long Id) {
		Company companyDB=companyRepo.findById(Id).orElseThrow(()->
                                 new ResourceNotFoundException("Company not found with Id: "+Id));
		   if (c.getRecruiter() == null ||
			        c.getRecruiter().getId() == null ||
			        !userRepo.existsById(c.getRecruiter().getId())) {
			        throw new ResourceNotFoundException("Recruiter not found");
			    }
		companyDB.setDescription(c.getDescription());
		companyDB.setLocation(c.getLocation());
		companyDB.setCompanyName(c.getCompanyName());
		companyDB.setRecruiter(c.getRecruiter());
		companyDB.setWebsite(c.getWebsite());
		return companyRepo.save(companyDB);
	}

	@Override
	public void deleteCompany(Long Id) {
		Company companyDB=companyRepo.findById(Id).orElseThrow(()->
                      new ResourceNotFoundException("Company not found with Id: "+Id));
		companyRepo.delete(companyDB);
		
	}

	
}
