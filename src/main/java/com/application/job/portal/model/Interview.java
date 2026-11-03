package com.application.job.portal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
public class Interview {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@NotNull(message = "Application is required")
	@ManyToOne
	@JoinColumn(name = "application_id")
	Application application;

	@NotNull(message = "Interview date is required")
	@Future(message = "Interview date must be in the future")
	LocalDateTime interviewDate;

	@NotBlank(message = "Interview mode is required")
	@Pattern(
	    regexp = "Online|Offline",
	    message = "Interview mode must be Online or Offline"
	)
	String interviewMode;

	@NotBlank(message = "Meeting link is required")
	String meetingLink;

	@NotBlank(message = "Interview status is required")
	String status;
	
	
}
