package com.application.job.portal.dto;

import java.time.LocalDateTime;

public record JobResponse(
        Long id,
        String title,
        String description,
        String location,
        Double salary,
        String jobType,
        Long companyId,
        String companyName,
        LocalDateTime postedAt
) {}