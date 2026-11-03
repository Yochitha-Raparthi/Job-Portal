package com.application.job.portal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	
	@NotBlank(message = "Name is required")
	@Size(min = 2, max = 50, message = "Name must contain 2 to 50 characters")
	@Column(nullable=false)
	String name;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email address")
	@Column(nullable=false, unique=true)
	String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must contain at least 8 characters")
	@Column(nullable=false)
	String password;
	
	@NotBlank(message = "Role is required")
	@Column(nullable=false)
	String role;
	
	@Column(nullable=false)
	LocalDateTime createdAt;
	
}
