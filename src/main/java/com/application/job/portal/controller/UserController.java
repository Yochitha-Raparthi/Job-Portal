
package com.application.job.portal.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.job.portal.dto.UserResponse;
import com.application.job.portal.model.User;
import com.application.job.portal.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Convert User entity into UserResponse DTO
    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    // Register User
    @PostMapping("/create")
    public UserResponse registerUser(@Valid @RequestBody User u) {
        User savedUser = userService.registerUser(u);
        return convertToResponse(savedUser);
    }

    // Get All Users
    @GetMapping("/allUsers")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get User By ID
    @GetMapping("/getUserById/{Id}")
    public UserResponse getUserById(@PathVariable Long Id) {
        User user = userService.getUserById(Id);
        return convertToResponse(user);
    }

    // Get User By Email
    @GetMapping("/getUserByEmail/{email}")
    public UserResponse getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return convertToResponse(user);
    }

    // Update User
    @PutMapping("/updateUser/{Id}")
    public UserResponse updateUser(
            @Valid @RequestBody User u,
            @PathVariable Long Id) {

        User updatedUser = userService.updateUser(u, Id);
        return convertToResponse(updatedUser);
    }

    // Delete User
    @DeleteMapping("/deleteUser/{Id}")
    public String deleteUser(@PathVariable Long Id) {
        userService.deleteUser(Id);
        return "User " + Id + " Deleted";
    }
}
