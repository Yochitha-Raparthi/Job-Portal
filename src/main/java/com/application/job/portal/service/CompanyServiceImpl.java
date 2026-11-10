//package com.application.job.portal.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.application.job.portal.exception.ResourceNotFoundException;
//import com.application.job.portal.model.Company;
//import com.application.job.portal.repo.CompanyRepository;
//import com.application.job.portal.repo.UserRepository;
//
//@Service
//public class CompanyServiceImpl implements CompanyService{
//	
//	private final CompanyRepository companyRepo;
//	private final UserRepository userRepo;
//
//	public CompanyServiceImpl(
//	        CompanyRepository companyRepository,
//	        UserRepository userRepository) {
//	    this.companyRepo = companyRepository;
//	    this.userRepo = userRepository;
//	}
//	
//	@Override
//	public Company createCompany(Company c) {
//		if (c.getRecruiter() == null ||
//			    !userRepo.existsById(c.getRecruiter().getId())) {
//			    throw new ResourceNotFoundException("Recruiter not found");
//			}
//		return companyRepo.save(c);
//	}
//
//	@Override
//	public List<Company> getAllCompanies() {
//		return companyRepo.findAll();
//	}
//
//	@Override
//	public Company getCompanyById(Long Id) {
//		return companyRepo.findById(Id).orElseThrow(()->
//		                                   new ResourceNotFoundException("Company not found with Id: "+Id));
//	}
//
//	@Override
//	public List<Company> getMyCompanies(Long recruiterId) {
//		return companyRepo.findByRecruiterId(recruiterId);
//	}
//
//	@Override
//	public Company updateCompany(Company c, Long Id) {
//		Company companyDB=companyRepo.findById(Id).orElseThrow(()->
//                                 new ResourceNotFoundException("Company not found with Id: "+Id));
//		   if (c.getRecruiter() == null ||
//			        c.getRecruiter().getId() == null ||
//			        !userRepo.existsById(c.getRecruiter().getId())) {
//			        throw new ResourceNotFoundException("Recruiter not found");
//			    }
//		companyDB.setDescription(c.getDescription());
//		companyDB.setLocation(c.getLocation());
//		companyDB.setCompanyName(c.getCompanyName());
//		companyDB.setRecruiter(c.getRecruiter());
//		companyDB.setWebsite(c.getWebsite());
//		return companyRepo.save(companyDB);
//	}
//
//	@Override
//	public void deleteCompany(Long Id) {
//		Company companyDB=companyRepo.findById(Id).orElseThrow(()->
//                      new ResourceNotFoundException("Company not found with Id: "+Id));
//		companyRepo.delete(companyDB);
//		
//	}
//
//	
//}


package com.application.job.portal.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.Company;
import com.application.job.portal.model.User;
import com.application.job.portal.repo.CompanyRepository;
import com.application.job.portal.repo.UserRepository;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepo;
    private final UserRepository userRepo;

    public CompanyServiceImpl(
            CompanyRepository companyRepo,
            UserRepository userRepo) {
        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Company createCompany(Company c, String email) {

        User recruiter = userRepo.findByEmail(email)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Recruiter not found"));

        // Do not trust recruiter details sent by the client.
        c.setRecruiter(recruiter);

        return companyRepo.save(c);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Company not found with Id: " + id));
    }

    @Override
    public List<Company> getMyCompanies(String email) {
        return companyRepo.findByRecruiterEmail(email);
    }

    @Override
    public Company updateCompany(Company c, Long id, String email) {

        Company companyDB = companyRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Company not found with Id: " + id));

        // Check whether the logged-in recruiter owns this company.
        if (!companyDB.getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot update another recruiter's company");
        }

        companyDB.setCompanyName(c.getCompanyName());
        companyDB.setDescription(c.getDescription());
        companyDB.setLocation(c.getLocation());
        companyDB.setWebsite(c.getWebsite());

        // Do not change the recruiter during an update.
        return companyRepo.save(companyDB);
    }

    @Override
    public void deleteCompany(Long id, String email) {

        Company companyDB = companyRepo.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Company not found with Id: " + id));

        if (!companyDB.getRecruiter().getEmail().equals(email)) {
            throw new AccessDeniedException(
                "You cannot delete another recruiter's company");
        }

        companyRepo.delete(companyDB);
    }
}

