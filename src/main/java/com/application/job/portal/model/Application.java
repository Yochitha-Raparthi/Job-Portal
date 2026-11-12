package com.application.job.portal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
    name = "application",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"user_id", "job_id"}
        )
    }
)
@Setter
@Getter
@NoArgsConstructor
public class Application {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	User applicant;

	@ManyToOne
	@JoinColumn(name = "job_id",nullable=false)
	Job job;

	@NotBlank(message = "Application status is required")
	String status;

	@NotBlank(message = "Resume URL is required")
	@Size(max = 500, message = "Resume URL cannot exceed 500 characters")
	@Pattern(
	    regexp = "^https?://.+",
	    message = "Resume URL must start with http:// or https://"
	)
	String resumeUrl;
	
	LocalDateTime appliedAt;
}
