package com.application.job.portal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
public class Job {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@NotBlank(message = "Job title is required")
	@Size(max = 100, message = "Job title cannot exceed 100 characters")
	String title;

	@NotBlank(message = "Job description is required")
	@Size(max = 2000, message = "Job description cannot exceed 2000 characters")
	String description;

	@NotBlank(message = "Job location is required")
	String location;

	@NotNull(message = "Salary is required")
	@Positive(message = "Salary must be greater than zero")
	Double salary;

	@NotBlank(message = "Job type is required")
	String jobType;

	@NotNull(message = "Company is required")
	@ManyToOne
	@JoinColumn(name = "company_id")
	Company company;
	
    LocalDateTime postedAt;
}
