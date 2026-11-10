//package com.application.job.portal.controller;
//
//import java.util.List;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.application.job.portal.model.Company;
//import com.application.job.portal.service.CompanyService;
//
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/company")
//public class CompanyController {
//
//	private final CompanyService companyService;
//	public CompanyController(CompanyService companyService) {
//		this.companyService=companyService;
//	}
//	
//	@PreAuthorize("hasRole('RECRUITER')")
//	@PostMapping("/create")
//	Company createCompany(@Valid @RequestBody Company c) {
//		return companyService.createCompany(c);
//	}
//	
//	@PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
//	@GetMapping("/allCompanies")
//	List<Company> getAllCompanies(){
//		return companyService.getAllCompanies();
//	}
//	
//	@PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
//	@GetMapping("/getCompanyById/{Id}")
//	Company getCompanyById(@PathVariable Long Id) {
//		return companyService.getCompanyById(Id);
//	}
//	
//	@PreAuthorize("hasRole('RECRUITER')")
//	@GetMapping("/getCompaniesByRecruiters/{recruiterId}")
//	List<Company> getCompaniesByRecruiters(@PathVariable Long recruiterId){
//		return companyService.getCompaniesByRecruiters(recruiterId);
//	}
//	
//	
//	@PreAuthorize("hasRole('RECRUITER')")
//	@PutMapping("/updateCompany/{Id}")
//	Company updateCompany(@Valid @RequestBody Company c, @PathVariable Long Id) {
//		return companyService.updateCompany(c, Id);
//	}
//	
//	@PreAuthorize("hasRole('RECRUITER')")
//	@DeleteMapping("/deleteCompany/{Id}")
//	String deleteCompany(@PathVariable Long Id) {
//		companyService.deleteCompany(Id);
//		return "Company "+Id+" Deleted";
//	}
//}


package com.application.job.portal.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.application.job.portal.model.Company;
import com.application.job.portal.service.CompanyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping("/create")
    public Company createCompany(
            @Valid @RequestBody Company c,
            Authentication authentication) {

        return companyService.createCompany(c, authentication.getName());
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/allCompanies")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getCompanyById/{id}")
    public Company getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    // A recruiter can see only their own companies through this endpoint.
    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/myCompanies")
    public List<Company> getMyCompanies(Authentication authentication) {
        return companyService.getMyCompanies(authentication.getName());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/updateCompany/{id}")
    public Company updateCompany(
            @Valid @RequestBody Company c,
            @PathVariable Long id,
            Authentication authentication) {

        return companyService.updateCompany(c, id, authentication.getName());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @DeleteMapping("/deleteCompany/{id}")
    public String deleteCompany(
            @PathVariable Long id,
            Authentication authentication) {

        companyService.deleteCompany(id, authentication.getName());
        return "Company " + id + " Deleted";
    }
}

