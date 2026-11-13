
package com.application.job.portal.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.job.portal.dto.LoginRequest;
import com.application.job.portal.dto.LoginResponse;
import com.application.job.portal.exception.ResourceNotFoundException;
import com.application.job.portal.model.User;
import com.application.job.portal.repo.UserRepository;
import com.application.job.portal.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public LoginController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        // Step 1: Authenticate email and password
        Authentication authentication =
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                    )
                );

        // Step 2: Get authenticated user details
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        // Step 3: Generate JWT token
        String token = jwtService.generateToken(userDetails);

        // Step 4: Fetch user from database
        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found")
                );

        // Step 5: Return token, role and name
        return new LoginResponse(
                token,
                "Bearer",
                "Login successful",
                user.getRole(),
                user.getName()
        );
    }
}
