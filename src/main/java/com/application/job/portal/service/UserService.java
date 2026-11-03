package com.application.job.portal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.job.portal.model.User;


public interface UserService {

	User registerUser(User u);
	List<User>getAllUsers();
	User getUserById(Long Id);
	User getUserByEmail(String email);
	User updateUser(User u, Long Id);
	void deleteUser(Long Id);
	
}
