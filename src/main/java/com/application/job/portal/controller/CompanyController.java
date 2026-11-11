package com.application.job.portal.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.application.job.portal.dto.CompanyResponse;
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
    public CompanyResponse createCompany(
            @Valid @RequestBody Company c,
            Authentication authentication) {

        return companyService.createCompany(c, authentication.getName());
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/allCompanies")
    public List<CompanyResponse> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'JOB_SEEKER')")
    @GetMapping("/getCompanyById/{id}")
    public CompanyResponse getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/myCompanies")
    public List<CompanyResponse> getMyCompanies(
            Authentication authentication) {

        return companyService.getMyCompanies(authentication.getName());
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @PutMapping("/updateCompany/{id}")
    public CompanyResponse updateCompany(
            @Valid @RequestBody Company c,
            @PathVariable Long id,
            Authentication authentication) {

        return companyService.updateCompany(
                c, id, authentication.getName());
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
