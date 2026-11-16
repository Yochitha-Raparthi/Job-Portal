
package com.application.job.portal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.User;
import com.application.job.portal.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepo,
            PasswordEncoder passwordEncoder) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(User u) {

        String email = u.getEmail().trim()
                .toLowerCase(Locale.ROOT);

        u.setEmail(email);

        if (userRepo.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "Email already exists"
            );
        }

        String role = u.getRole();

        if (!"RECRUITER".equals(role)
                && !"JOB_SEEKER".equals(role)) {

            throw new IllegalArgumentException(
                    "Invalid registration role"
            );
        }

        // Ignore any ID or creation timestamp sent by client.
        u.setId(null);

        u.setPassword(
                passwordEncoder.encode(u.getPassword())
        );

        u.setCreatedAt(LocalDateTime.now());

        return userRepo.save(u);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }

    @Override
    @Transactional
    public User updateProfile(Long id, String name) {

        User userDB = getUserById(id);

        userDB.setName(name.trim());

        return userRepo.save(userDB);
    }

    // Legacy method retained for interface compatibility.
    // It no longer changes email, password, or role.
    @Override
    @Transactional
    public User updateUser(User u, Long id) {

        User userDB = getUserById(id);

        userDB.setName(u.getName().trim());

        return userRepo.save(userDB);
    }

    @Override
    public void deleteUser(Long id) {
        throw new UnsupportedOperationException(
                "Account deletion is temporarily disabled"
        );
    }
}
