package com.application.job.portal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
public class Company {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@NotBlank(message = "Company name is required")
	@Size(min = 2, max = 100, message = "Company name must be 2 to 100 characters")
	String companyName;

	@NotBlank(message = "Location is required")
	String location;

	@NotBlank(message = "Description is required")
	@Size(max = 1000, message = "Description cannot exceed 1000 characters")
	String description;

	@NotBlank(message = "Website is required")
	@Pattern(
	    regexp = "^https?://.+\\..+",
	    message = "Enter a valid website URL starting with http:// or https://"
	)
	String website;

	@ManyToOne
	@JoinColumn(name = "recruiter_id", nullable = false)
	User recruiter;
}
