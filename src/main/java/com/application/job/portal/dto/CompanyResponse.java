package com.application.job.portal.dto;

public record CompanyResponse(
    Long id,
    String companyName,
    String location,
    String description,
    String website,
    Long recruiterId,
    String recruiterName
) {}