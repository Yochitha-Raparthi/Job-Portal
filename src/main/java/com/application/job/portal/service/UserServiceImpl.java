package com.application.job.portal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.User;
import com.application.job.portal.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(
	        UserRepository userRepo,
	        PasswordEncoder passwordEncoder) {
	    this.userRepo = userRepo;
	    this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User registerUser(User u) {
		if (userRepo.findByEmail(u.getEmail()).isPresent()) {
		    throw new IllegalArgumentException("Email already exists");
		}
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		u.setCreatedAt(LocalDateTime.now());
		return userRepo.save(u);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public User getUserById(Long Id) {
		return userRepo.findById(Id).orElseThrow(()->
		                      new ResourceNotFoundException("user not found with ID: " + Id));
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email).orElseThrow(()->
                            new ResourceNotFoundException("user not found with email:" + email));
	}

	@Override
	public User updateUser(User u, Long Id) {
	    User userDB=userRepo.findById(Id).orElseThrow(()->
                                 new ResourceNotFoundException("user not found with ID:" + Id));
	    
	    if (u.getPassword() != null && !u.getPassword().isBlank()) {
	        userDB.setPassword(passwordEncoder.encode(u.getPassword()));
	    }
	    userDB.setEmail(u.getEmail());
	    	userDB.setName(u.getName());
	    userDB.setRole(u.getRole());
		return userRepo.save(userDB);
	}

	@Override
	public void deleteUser(Long Id) {
		 User userDB = userRepo.findById(Id)
			        .orElseThrow(() ->
			            new ResourceNotFoundException("User not found with ID: " + Id));
		userRepo.delete(userDB);
		
	}

}
