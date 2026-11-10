package com.application.job.portal.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.job.portal.model.User;

public interface UserRepository extends JpaRepository<User,Long>{
     Optional<User>findByEmail(String email);
     
}
